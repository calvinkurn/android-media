package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.topads.data.factory.TopAdsShopAdFactory;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailShopMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
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
        TopAdsManagementService topAdsManagementService = new TopAdsManagementService();
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();
        TopAdsDetailShopMapper mapper = new TopAdsDetailShopMapper();
        TopAdsShopAdFactory topAdsShopAdFactory = new TopAdsShopAdFactory(context, topAdsManagementApi, mapper);
        TopAdsShopAdsRepository topAdsGroupAdsRepository = new TopAdsShopAdsRepositoryImpl(topAdsShopAdFactory);
        TopAdsGetDetailShopUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsGetDetailShopUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailShopUseCase topAdsSaveDetailShopUseCase = new TopAdsSaveDetailShopUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        return new TopAdsDetailNewShopPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsSaveDetailShopUseCase);
    }
}
