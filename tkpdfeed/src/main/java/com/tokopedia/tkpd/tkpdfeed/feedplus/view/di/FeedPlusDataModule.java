package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;
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


}
