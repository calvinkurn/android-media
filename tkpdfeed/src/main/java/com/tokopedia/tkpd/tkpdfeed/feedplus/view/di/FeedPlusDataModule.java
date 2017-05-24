package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;

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

//    @FeedPlusScope
//    @Provides
//    FeedRepository provideFeedRepository(FeedFactory feedFactory) {
//        return new FeedRepositoryImpl(feedFactory);
//    }
//
//    @FeedPlusScope
//    @Provides
//    FeedFactory provideFeedFactory(@ActivityContext  Context context,
//                                   ApolloClient apolloClient,
//                                   FeedListMapper feedListMapper,
//                                   FeedDetailListMapper feedDetailListMapper) {
//        return new FeedFactory(context, apolloClient, feedListMapper, feedDetailListMapper);
//    }
//
//    @FeedPlusScope
//    @Provides
//    FeedListMapper provideFeedListMapper() {
//        return new FeedListMapper();
//    }
}
