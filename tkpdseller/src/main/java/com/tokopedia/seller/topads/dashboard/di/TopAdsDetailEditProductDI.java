package com.tokopedia.seller.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.dashboard.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailEditProductDI {

    public static TopAdsDetailEditProductPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();
        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context).getAccessToken(context));
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();
        TopAdsDetailProductMapper topAdsDetailProductMapper = new TopAdsDetailProductMapper();
        SearchProductEOFMapper searchProductEOFMapper = new SearchProductEOFMapper();
        TopAdsBulkActionMapper topAdsBulkActionMapper = new TopAdsBulkActionMapper();
        CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource = new CloudTopAdsSearchProductDataSource(context, topAdsManagementService,searchProductEOFMapper);
        TopAdsSearchProductRepository topAdsSearchProductRepository = new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);
        TopAdsProductAdFactory topAdsShopAdFactory = new TopAdsProductAdFactory(context, topAdsManagementApi, topAdsDetailProductMapper, topAdsBulkActionMapper);
        TopAdsProductAdsRepository topAdsGroupAdsRepository = new TopAdsProductAdsRepositoryImpl(topAdsShopAdFactory);
        TopAdsGetDetailProductUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsGetDetailProductUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailProductUseCase topAdsSaveDetailShopUseCase = new TopAdsSaveDetailProductUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsProductListUseCase topAdsProductListUseCase = new TopAdsProductListUseCase(threadExecutor,postExecutionThread,topAdsSearchProductRepository);
        return new TopAdsDetailEditProductPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsSaveDetailShopUseCase, topAdsProductListUseCase);
    }
}
