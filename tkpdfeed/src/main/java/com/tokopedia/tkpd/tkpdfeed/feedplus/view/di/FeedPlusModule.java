package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FavoriteShopFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.WishlistFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.AddWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RemoveWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FavoriteShopRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FavoriteShopRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.WishlistRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.WishlistRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsCloudUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RefreshFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RemoveWishlistUseCase;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @FeedPlusScope
    @Provides
    MojitoService provideRecentProductService(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoService.class);
    }

    @FeedPlusScope
    @Provides
    RecentProductMapper provideRecentProductMapper(Gson gson) {
        return new RecentProductMapper(gson);
    }

    @FeedPlusScope
    @Provides
    FeedRepository provideFeedRepository(FeedFactory feedFactory) {
        return new FeedRepositoryImpl(feedFactory);
    }

    @FeedPlusScope
    @Provides
    FeedFactory provideFeedFactory(@ApplicationContext Context context,
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   @Named(NAME_CLOUD) FeedResultMapper feedResultMapperCloud,
                                   @Named(NAME_LOCAL) FeedResultMapper feedResultMapperLocal,
                                   FeedDetailListMapper feedDetailListMapper,
                                   GlobalCacheManager globalCacheManager,
                                   MojitoService mojitoService,
                                   RecentProductMapper recentProductMapper,
                                   CheckNewFeedMapper checkNewFeedMapper) {
        return new FeedFactory(
                context,
                apolloClient,
                feedListMapper,
                feedResultMapperCloud,
                feedResultMapperLocal,
                globalCacheManager,
                feedDetailListMapper,
                mojitoService,
                recentProductMapper,
                checkNewFeedMapper
        );
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
    FavoriteShopMapper provideFavoriteShopMapper() {
        return new FavoriteShopMapper();
    }

    @FeedPlusScope
    @Provides
    ActionService provideActionService() {
        return new ActionService();
    }

    @FeedPlusScope
    @Provides
    FavoriteShopFactory provideFavoriteShopFactory(@ApplicationContext Context context,
                                                   FavoriteShopMapper mapper,
                                                   ActionService service) {
        return new FavoriteShopFactory(context, mapper, service);
    }

    @FeedPlusScope
    @Provides
    FavoriteShopRepository provideFavoriteShopRepository(FavoriteShopFactory favoriteShopFactory) {
        return new FavoriteShopRepositoryImpl(favoriteShopFactory);
    }

    @FeedPlusScope
    @Provides
    AddWishlistMapper provideAddWishlistMapper() {
        return new AddWishlistMapper();
    }

    @FeedPlusScope
    @Provides
    RemoveWishlistMapper provideRemoveWishlistMapper() {
        return new RemoveWishlistMapper();
    }

    @FeedPlusScope
    @Provides
    MojitoNoRetryAuthService provideMojitoNoRetryAuthService() {
        return new MojitoNoRetryAuthService();
    }

    @FeedPlusScope
    @Provides
    WishlistFactory provideWishlistFactory(AddWishlistMapper addWishlistMapper,
                                           RemoveWishlistMapper removeWishlistMapper,
                                           MojitoNoRetryAuthService mojitoNoRetryAuthService) {
        return new WishlistFactory(addWishlistMapper,
                removeWishlistMapper,
                mojitoNoRetryAuthService);
    }

    @FeedPlusScope
    @Provides
    WishlistRepository provideWishlistRepository(WishlistFactory wishlistFactory) {
        return new WishlistRepositoryImpl(wishlistFactory);
    }


    @FeedPlusScope
    @Provides
    GetFeedsUseCase provideGetFeedsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            FeedRepository feedRepository) {
        return new GetFeedsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @FeedPlusScope
    @Provides
    GetFirstPageFeedsUseCase provideGetFirstPageFeedsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            FeedRepository feedRepository,
            GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase) {
        return new GetFirstPageFeedsUseCase(threadExecutor, postExecutionThread,
                feedRepository, getFirstPageFeedsCloudUseCase);
    }

    @FeedPlusScope
    @Provides
    GetRecentViewUseCase provideGetRecentProductUseCase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        FeedRepository feedRepository) {
        return new GetRecentViewUseCase(threadExecutor,
                postExecutionThread,
                feedRepository);
    }

    @FeedPlusScope
    @Provides
    GetFirstPageFeedsCloudUseCase provideGetFirstPageFeedsCloudUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       FeedRepository feedRepository,
                                                                       GetRecentViewUseCase getRecentProductUsecase) {
        return new GetFirstPageFeedsCloudUseCase(
                threadExecutor, postExecutionThread,
                feedRepository,
                getRecentProductUsecase);
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
                                                     FavoriteShopRepository repository) {
        return new FavoriteShopUseCase(threadExecutor, postExecutionThread, repository);
    }


    @FeedPlusScope
    @Provides
    AddWishlistUseCase provideAddWishlistUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 WishlistRepository wishlistRepository) {
        return new AddWishlistUseCase(threadExecutor,
                postExecutionThread,
                wishlistRepository);
    }

    @FeedPlusScope
    @Provides
    RemoveWishlistUseCase provideRemoveWishlistUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       WishlistRepository wishlistRepository) {
        return new RemoveWishlistUseCase(threadExecutor,
                postExecutionThread,
                wishlistRepository);
    }

    @FeedPlusScope
    @Provides
    RefreshFeedUseCase provideRefreshFeedUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 FeedRepository feedRepository) {
        return new RefreshFeedUseCase(threadExecutor,
                postExecutionThread,
                feedRepository);
    }

    @FeedPlusScope
    @Provides
    CheckNewFeedMapper provideCheckNewFeedMapper() {
        return new CheckNewFeedMapper();
    }

    @FeedPlusScope
    @Provides
    CheckNewFeedUseCase provideCheckNewFeedUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   FeedRepository feedRepository) {
        return new CheckNewFeedUseCase(threadExecutor,
                postExecutionThread,
                feedRepository);
    }

}
