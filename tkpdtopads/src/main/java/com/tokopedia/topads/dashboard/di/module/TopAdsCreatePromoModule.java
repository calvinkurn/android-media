package com.tokopedia.topads.dashboard.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.topads.dashboard.data.factory.TopAdsShopAdFactory;
import com.tokopedia.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.topads.dashboard.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.topads.dashboard.di.scope.TopAdsDashboardScope;
import com.tokopedia.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsGetSuggestionUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewProductPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenterImpl;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsGetProductDetailPresenterImpl;

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
                                                           TopAdsProductListUseCase topAdsProductListUseCase,
                                                           TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        return new TopAdsDetailNewGroupPresenterImpl(topAdsCreateNewGroupUseCase, topAdsGetDetailGroupUseCase,
                topAdsSaveDetailGroupUseCase, topAdsCreateDetailProductListUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
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
                                                                        TopAdsProductListUseCase topAdsProductListUseCase,
                                                                        TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase) {
        return new TopAdsDetailNewProductPresenterImpl(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsCreateDetailProductListUseCase,
                topAdsProductListUseCase, topAdsGetSuggestionUseCase);
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
                                                                             TopAdsProductListUseCase topAdsProductListUseCase,
                                                                             TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase){
        return new TopAdsDetailEditProductPresenterImpl(topAdsGetDetailProductUseCase, topAdsSaveDetailProductUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsDetailEditGroupPresenter provideTopadsDetailEditGroupPresenter(TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase,
                                                                           TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase,
                                                                           TopAdsProductListUseCase topAdsProductListUseCase,
                                                                         TopAdsGetSuggestionUseCase topAdsGetSuggestionUseCase){
        return new TopAdsDetailEditGroupPresenterImpl(topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase, topAdsProductListUseCase, topAdsGetSuggestionUseCase);
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
    TopAdsSearchProductRepository provideTopAdsSearchProductRepository(@ApplicationContext Context context,
                                                                       CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource) {
        return new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);
    }

    @TopAdsDashboardScope
    @Provides
    TopAdsManagementService provideTopAdsManagementService(@ApplicationContext Context context) {
        return new TopAdsManagementService(new SessionHandler(context));
    }

}
