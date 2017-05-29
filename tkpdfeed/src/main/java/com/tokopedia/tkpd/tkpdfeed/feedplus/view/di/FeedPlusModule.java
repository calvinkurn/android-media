package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ApplicationScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.module.OkHttpClientModule;
import com.tokopedia.core.network.di.module.OkHttpClientModule;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FavoriteShopFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FavoriteShopRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FavoriteShopRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;

import javax.inject.Named;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @FeedPlusScope
    @Provides
    SessionHandler provideSessionHandler(@ApplicationContext Context context) {
        return new SessionHandler(context);
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
    FeedFactory provideFeedFactory(@ActivityContext Context context,
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   @Named(NAME_CLOUD) FeedResultMapper feedResultMapperCloud,
                                   @Named(NAME_LOCAL) FeedResultMapper feedResultMapperLocal,
                                   FeedDetailListMapper feedDetailListMapper,
                                   GlobalCacheManager globalCacheManager) {
        return new FeedFactory(
                context,
                apolloClient,
                feedListMapper,
                feedResultMapperCloud,
                feedResultMapperLocal,
                globalCacheManager,
                feedDetailListMapper);
    }

    @FeedPlusScope
    @Provides
    FeedListMapper provideFeedListMapper() {
        return new FeedListMapper();
    }

    @FeedPlusScope
    @Named(NAME_LOCAL)
    @Provides
    FeedResultMapper provideLocalFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_LOCAL);
    }

    @FeedPlusScope
    @Named(NAME_CLOUD)
    @Provides
    FeedResultMapper provideCloudFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }


    @FeedPlusScope
    @Provides
    FeedDetailListMapper provideFeedDetailListMapper() {
        return new FeedDetailListMapper();
    }


    @FeedPlusScope
    @Provides
    GetFeedsDetailUseCase provideGetFeedsDetailUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       FeedRepository feedRepository) {
        return new GetFeedsDetailUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @FeedPlusScope
    @Provides
    FavoriteShopUseCase provideDoFavoriteShopUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     FavoriteShopRepository repository){
        return new FavoriteShopUseCase(threadExecutor, postExecutionThread, repository);
    }

    @FeedPlusScope
    @Provides
    FavoriteShopMapper provideFavoriteShopMapper(){
        return new FavoriteShopMapper();
    }

    @FeedPlusScope
    @Provides
    ActionService provideActionService(){
        return new ActionService();
    }

    @FeedPlusScope
    @Provides
    FavoriteShopFactory provideFavoriteShopFactory(@ActivityContext Context context, FavoriteShopMapper mapper, ActionService service){
        return new FavoriteShopFactory(context, mapper, service);
    }

    @FeedPlusScope
    @Provides
    FavoriteShopRepository provideFavoriteShopRepository(FavoriteShopFactory favoriteShopFactory) {
        return new FavoriteShopRepositoryImpl(favoriteShopFactory);
    }
}
