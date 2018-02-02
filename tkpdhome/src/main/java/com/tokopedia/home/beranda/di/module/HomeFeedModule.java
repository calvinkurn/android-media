package com.tokopedia.home.beranda.di.module;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.HomeFeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetHomeFeedsUseCase;

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
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            HomeFeedRepository feedRepository) {
        return new GetHomeFeedsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }
}