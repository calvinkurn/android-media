package com.tokopedia.seller.topads.dashboard.di;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.dashboard.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateDetailProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailNewGroupDI {

    public static TopAdsDetailNewGroupPresenter createPresenter(BaseActivity context) {
        AppComponent component = context.getApplicationComponent();

        ThreadExecutor threadExecutor = component.threadExecutor();
        PostExecutionThread postExecutionThread = component.postExecutionThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context).getAccessToken(context));
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();
        TopAdsDetailProductMapper topAdsDetailProductMapper = new TopAdsDetailProductMapper();
        TopAdsBulkActionMapper topAdsBulkActionMapper = new TopAdsBulkActionMapper();
        TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper = new TopAdsDetailGroupDomainMapper();

        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper, topAdsDetailGroupDomainMapper);
        TopAdsProductAdFactory topAdsShopAdFactory = new TopAdsProductAdFactory(context, topAdsManagementApi, topAdsDetailProductMapper, topAdsBulkActionMapper);
        SearchProductEOFMapper searchProductEOFMapper = new SearchProductEOFMapper();
        CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource = new CloudTopAdsSearchProductDataSource(context, topAdsManagementService,searchProductEOFMapper);
        TopAdsSearchProductRepository topAdsSearchProductRepository = new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
        TopAdsProductAdsRepository topAdsProductAdsRepository = new TopAdsProductAdsRepositoryImpl(topAdsShopAdFactory);

        TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase = new TopAdsGetDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase = new TopAdsSaveDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);

        TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase = new TopAdsCreateNewGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsCreateDetailProductListUseCase topAdsCreateDetailProductListUseCase = new TopAdsCreateDetailProductListUseCase(threadExecutor,postExecutionThread, topAdsProductAdsRepository );
        TopAdsProductListUseCase topAdsProductListUseCase = new TopAdsProductListUseCase(threadExecutor,postExecutionThread,topAdsSearchProductRepository);

        return new TopAdsDetailNewGroupPresenterImpl(topAdsCreateNewGroupUseCase, topAdsGetDetailGroupUseCase,
                topAdsSaveDetailGroupUseCase,topAdsCreateDetailProductListUseCase, topAdsProductListUseCase);
    }
}

