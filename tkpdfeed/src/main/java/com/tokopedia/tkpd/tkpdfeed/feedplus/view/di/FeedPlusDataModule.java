package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author ricoharisin .
 */

@FeedPlusScope
@Module(includes = {FeedPlusModule.class})
public class FeedPlusDataModule {

    @FeedPlusScope
    @Provides
    GetFeedsUseCase provideGetFeedsUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           FeedRepository feedRepository) {
        return new GetFeedsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @FeedPlusScope
    @Provides
    GetFirstPageFeedsUseCase provideGetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             FeedRepository feedRepository) {
        return new GetFirstPageFeedsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @FeedPlusScope
    @Provides
    FeedRepository provideFeedRepository(FeedFactory feedFactory) {
        return new FeedRepositoryImpl(feedFactory);
    }

    @FeedPlusScope
    @Provides
    FeedFactory provideFeedFactory(@ActivityContext  Context context,
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   @Named("CLOUD") FeedResultMapper feedResultMapperCloud,
                                   @Named("LOCAL") FeedResultMapper feedResultMapperLocal,
                                   GlobalCacheManager globalCacheManager) {
        return new FeedFactory(context, apolloClient, feedListMapper,
                feedResultMapperCloud, feedResultMapperLocal, globalCacheManager);
    }

    @FeedPlusScope
    @Provides
    FeedListMapper provideFeedListMapper() {
        return new FeedListMapper();
    }

    @FeedPlusScope
    @Named("LOCAL")
    @Provides
    FeedResultMapper provideLocalFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_LOCAL);
    }

    @FeedPlusScope
    @Named("CLOUD")
    @Provides
    FeedResultMapper provideCloudFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }
}
