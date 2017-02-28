package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.topads.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.data.factory.TopAdsShopAdFactory;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsProductAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.repository.TopAdsShopAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailShopUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailProductUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailShopUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoProductPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoProductPresenterImpl;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoShopPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsEditPromoShopPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsEditPromoProductDI {

    public static TopAdsEditPromoProductPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService();
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsDetailProductMapper topAdsDetailProductMapper = new TopAdsDetailProductMapper();

        TopAdsProductAdFactory topAdsShopAdFactory = new TopAdsProductAdFactory(context, topAdsManagementApi, topAdsDetailProductMapper);

        TopAdsProductAdsRepository topAdsGroupAdsRepository = new TopAdsProductAdsRepositoryImpl(topAdsShopAdFactory);

        TopAdsGetDetailProductUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsGetDetailProductUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailProductUseCase topAdsSaveDetailShopUseCase = new TopAdsSaveDetailProductUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        return new TopAdsEditPromoProductPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsSaveDetailShopUseCase);
    }
}
