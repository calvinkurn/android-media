package com.tokopedia.seller.goldmerchant.statistic.di.module;

import android.content.Context;
import android.content.res.AssetManager;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticScope;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetBuyerGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetPopularProductUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetProductGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphShopInfoUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionGraphUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatGetTransactionTableUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.interactor.GMStatMarketInsightShopInfoUseCase;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMDashboardPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMDashboardPresenterImpl;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionPresenterImpl;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenterImpl;
import com.tokopedia.seller.product.data.repository.CategoryRepositoryImpl;
import com.tokopedia.seller.product.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.data.source.CategoryDataSource;
import com.tokopedia.seller.product.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.data.source.FetchCategoryDataSource;
import com.tokopedia.seller.product.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.category.domain.CategoryRepository;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractor;
import com.tokopedia.seller.topads.dashboard.domain.interactor.DashboardTopadsInteractorImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author normansyahputa on 7/6/17.
 */
@GMStatisticScope
@Module
public class GMStatisticModule {

    @GMStatisticScope
    @Provides
    GMStatApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }

    @GMStatisticScope
    @Provides
    GMStatRepository provideGMStatRepository(GMStatDataSource gmStatDataSource,
                                             GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                             GMTransactionTableMapper gmTransactionTableMapper,
                                             ShopInfoRepository shopInfoRepository) {
        return new GMStatRepositoryImpl(gmTransactionStatDomainMapper, gmStatDataSource, gmTransactionTableMapper,
                shopInfoRepository);
    }

    @GMStatisticScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ActivityContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMStatisticScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @GMStatisticScope
    @Provides
    GMStatisticTransactionTablePresenter provideGmStatisticTransactionTablePresenter(
            GMStatGetTransactionTableUseCase gmStatGetTransactionTableUseCase
    ) {
        return new GMStatisticTransactionTablePresenterImpl(gmStatGetTransactionTableUseCase);
    }

    @GMStatisticScope
    @Provides
    DashboardTopadsInteractor provideDashboardTopadsInteractor(@ApplicationContext Context context) {
        return new DashboardTopadsInteractorImpl(context);
    }

    @GMStatisticScope
    @Provides
    GMStatisticTransactionPresenter provideGmStatisticTransactionPresenter(
            GMStatGetTransactionGraphUseCase gmStatGetTransactionGraphUseCase,
            DashboardTopadsInteractor dashboardTopadsInteractor,
            SessionHandler sessionHandler) {
        return new GMStatisticTransactionPresenterImpl(gmStatGetTransactionGraphUseCase, dashboardTopadsInteractor, sessionHandler);
    }

    @GMStatisticScope
    @Provides
    public GMDashboardPresenter provideGmFragmentPresenter(GMStatMarketInsightShopInfoUseCase marketInsightShopInfoUseCase,
                                                           GMStatGetBuyerGraphUseCase buyerGraphUseCase,
                                                           GMStatGetPopularProductUseCase popularProductUseCase,
                                                           GMStatGetTransactionGraphShopInfoUseCase getTransactionGraphShopInfoUseCase,
                                                           GMStatGetProductGraphUseCase productGraphUseCase,
                                                           AddProductShopInfoUseCase shopInfoUseCase) {
        return new GMDashboardPresenterImpl(marketInsightShopInfoUseCase, buyerGraphUseCase, popularProductUseCase,
                getTransactionGraphShopInfoUseCase, productGraphUseCase, shopInfoUseCase);
    }

    @GMStatisticScope
    @Provides
    public AssetManager provideAssetManager(@ApplicationContext Context context) {
        return context.getAssets();
    }

    @GMStatisticScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource, CategoryDataSource categoryDataSource, FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @GMStatisticScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }
}
