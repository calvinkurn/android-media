package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.di.module.InterceptorModule;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError;
import com.tokopedia.home.beranda.di.DefaultAuthWithErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.HomeFeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetHomeFeedsUseCase;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by henrypriyono on 12/27/17.
 */

@Module
public class HomeFeedModule {

    @Provides
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @Provides
    HomeFeedRepository provideHomeFeedRepository(HomeFeedFactory feedFactory) {
        return new HomeFeedRepositoryImpl(feedFactory);
    }

    @Provides
    HomeFeedFactory provideHomeFeedFactory(ApolloClient apolloClient,
                                           HomeFeedMapper homeFeedMapper,
                                           FeedResultMapper feedResultMapper) {
        return new HomeFeedFactory(
                apolloClient,
                homeFeedMapper,
                feedResultMapper
        );
    }

    @Provides
    HomeFeedMapper provideHomeFeedMapper() {
        return new HomeFeedMapper();
    }

    @Provides
    FeedResultMapper provideFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }

    @Provides
    GetHomeFeedsUseCase provideGetHomeFeedsUseCase(
            HomeFeedRepository feedRepository) {
        return new GetHomeFeedsUseCase(new JobExecutor(), new UIThread(), feedRepository);
    }

    @DefaultAuthWithErrorHandler
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationContext Context context,
                                     CacheApiInterceptor cacheApiInterceptor) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, DeveloperOptions.CHUCK_ENABLED);
        return OkHttpFactory.create().buildDaggerClientDefaultAuthWithErrorHandler(
                new FingerprintInterceptor(),
                new TkpdAuthInterceptor(),
                OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy(),
                new ChuckInterceptor(context).showNotification(
                        localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false)
                ),
                new DebugInterceptor(),
                new TkpdErrorResponseInterceptor(TkpdV4ResponseError.class),
                cacheApiInterceptor
        );
    }

}