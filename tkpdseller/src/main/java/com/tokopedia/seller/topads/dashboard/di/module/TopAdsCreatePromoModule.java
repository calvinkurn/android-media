package com.tokopedia.seller.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsShopAdFactory;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.dashboard.di.scope.TopAdsDashboardScope;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenterImpl;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

@TopAdsDashboardScope
@Module
public class TopAdsCreatePromoModule {
    @TopAdsDashboardScope
    @Provides
    TopAdsDetailNewGroupPresenter providePresenterNewGroup(TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase,
                                                           TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                                           TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                                           TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                                           TopAdsProductListUseCase topAdsProductListUseCase) {
        return new TopAdsDetailNewGroupPresenterImpl(topAdsCreateNewGroupUseCase, topAdsGetDetailGroupUseCase,
                topAdsSaveDetailGroupUseCase, topAdsCreateDetailProductListUseCase, topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsGetProductDetailPresenter provideTopAdsGetProductDetailPresenter(TopAdsProductListUseCase topAdsProductListUseCase){
        return new TopAdsGetProductDetailPresenterImpl(topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailNewProductPresenter provideTopAdsDetailProductPresenter(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                                        TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                                        TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase,
                                                                        TopAdsProductListUseCase topAdsProductListUseCase) {
        return new TopAdsDetailNewProductPresenterImpl(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsCreateDetailProductListUseCase,
                topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailNewShopPresenter provideTopAdsDetailShopPresenter(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                                                     TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                                                     TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase,
                                                                     TopAdsProductListUseCase topAdsProductListUseCase) {
        return new TopAdsDetailNewShopPresenterImpl(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase, topAdsCreateDetailShopUseCase,
                topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailEditProductPresenter provideTopadsDetailEditProductPresenter(TopAdsGetDetailProductUseCase topAdsGetDetailProductUseCase,
                                                                             TopAdsSaveDetailProductUseCase topAdsSaveDetailProductUseCase,
                                                                             TopAdsProductListUseCase topAdsProductListUseCase){
        return new TopAdsDetailEditProductPresenterImpl(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailEditGroupPresenter provideTopadsDetailEditGroupPresenter(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                                                           TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                                                           TopAdsProductListUseCase topAdsProductListUseCase){
        return new TopAdsDetailEditGroupPresenterImpl(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase, topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailEditShopPresenter provideTopAdsDetailEditShopPresenter(TopAdsGetDetailShopUseCase topAdsGetDetailShopUseCase,
                                                                          TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase,
                                                                          TopAdsProductListUseCase topAdsProductListUseCase){
        return new TopAdsDetailEditShopPresenterImpl(topAdsGetDetailShopUseCase, topAdsSaveDetailShopUseCase, topAdsProductListUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsShopAdsRepository provideTopAdsShopRepository(TopAdsShopAdFactory topAdsShopAdFactory) {
        return new TopAdsShopAdsRepositoryImpl(topAdsShopAdFactory);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsGroupAdsRepository provideTopAdsGroupRepository(TopAdsGroupAdFactory topAdsGroupAdFactory) {
        return new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsManagementApi provideTopAdsManagementApi(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsManagementApi.class);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsProductAdsRepository provideTopAdsProductRepository(TopAdsProductAdFactory topAdsProductAdFactory) {
        return new TopAdsProductAdsRepositoryImpl(topAdsProductAdFactory);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsSearchProductRepository provideTopAdsSearchProductRepository(@ActivityContext Context context,
                                                                       CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {
        return new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsManagementService provideTopAdsManagementService(@ActivityContext Context context) {
        return new TopAdsManagementService(new SessionHandler(context).getAccessToken(context));
    }

}
