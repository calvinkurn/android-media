package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.module.OkHttpClientModule;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @FeedPlusScope
    @Provides
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @FeedPlusScope
    @Provides
    FeedListMapper provideFeedListMapper() {
        return new FeedListMapper();
    }

    @FeedPlusScope
    @Provides
    FeedDetailListMapper provideFeedDetailListMapper() {
        return new FeedDetailListMapper();
    }

    @FeedPlusScope
    @Provides
    FeedFactory provideFeedFactory(@ActivityContext Context context,
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   FeedDetailListMapper feedDetailListMapper) {
        return new FeedFactory(
                context,
                apolloClient,
                feedListMapper,
                feedDetailListMapper);
    }

    @FeedPlusScope
    @Provides
    FeedRepository provideFeedRepository(FeedFactory feedFactory) {
        return new FeedRepositoryImpl(feedFactory);
    }

    @FeedPlusScope
    @Provides
    GetFeedsDetailUseCase provideGetFeedsDetailUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       FeedRepository feedRepository) {
        return new GetFeedsDetailUseCase(threadExecutor, postExecutionThread, feedRepository);
    }
}
