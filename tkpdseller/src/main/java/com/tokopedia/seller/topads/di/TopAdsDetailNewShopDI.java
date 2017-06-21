package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.data.factory.TopAdsShopAdFactory;
import com.tokopedia.seller.topads.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailShopMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditShopPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditShopPresenterImpl;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewShopPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewShopPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailNewShopDI {

    public static TopAdsDetailNewShopPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();
        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context).getAccessToken(context));
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();
        TopAdsDetailShopMapper mapper = new TopAdsDetailShopMapper();
        TopAdsShopAdFactory topAdsShopAdFactory = new TopAdsShopAdFactory(context, topAdsManagementApi, mapper);
        SearchProductEOFMapper searchProductEOFMapper = new SearchProductEOFMapper();
        CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource = new CloudTopAdsSearchProductDataSource(context, topAdsManagementService,searchProductEOFMapper);
        TopAdsSearchProductRepository topAdsSearchProductRepository = new TopAdsSearchProductRepositoryImpl(context, cloudTopAdsSearchProductDataSource);
        TopAdsShopAdsRepository topAdsShopAdsRepository = new TopAdsShopAdsRepositoryImpl(topAdsShopAdFactory);
        TopAdsGetDetailShopUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsGetDetailShopUseCase(threadExecutor, postExecutionThread, topAdsShopAdsRepository);
        TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase = new TopAdsSaveDetailShopUseCase(threadExecutor, postExecutionThread, topAdsShopAdsRepository);
        TopAdsCreateDetailShopUseCase topAdsCreateDetailShopUseCase = new TopAdsCreateDetailShopUseCase(threadExecutor, postExecutionThread, topAdsShopAdsRepository);
        TopAdsProductListUseCase topAdsProductListUseCase = new TopAdsProductListUseCase(threadExecutor,postExecutionThread,topAdsSearchProductRepository);
        return new TopAdsDetailNewShopPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsSaveDetailShopUseCase, topAdsCreateDetailShopUseCase, topAdsProductListUseCase);
    }
}
