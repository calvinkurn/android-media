package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.topads.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.seller.topads.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsEtalaseListRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsShopService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.domain.TopAdsEtalaseListRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsEtalaseListUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsEtalaseListPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsEtalaseListPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsGetEtalaseListDI {
    public static TopAdsEtalaseListPresenter createPresenter(Context context) {
        // Gson gson = new Gson();

        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsShopService topAdsShopService = new TopAdsShopService();
        TopAdsShopApi topAdsShopApi = topAdsShopService.getApi();

        TopAdsEtalaseListMapper topAdsEtalaseListMapper = new TopAdsEtalaseListMapper();

        TopAdsEtalaseFactory topAdsEtalaseFactory = new TopAdsEtalaseFactory(topAdsShopApi, topAdsEtalaseListMapper);

        TopAdsEtalaseListRepository topAdsEtalaseListRepository = new TopAdsEtalaseListRepositoryImpl(topAdsEtalaseFactory);

        TopAdsEtalaseListUseCase topAdsEtalaseListUseCase =
                new TopAdsEtalaseListUseCase(
                        threadExecutor,
                        postExecutionThread,
                        topAdsEtalaseListRepository);

        return new TopAdsEtalaseListPresenterImpl(topAdsEtalaseListUseCase);
    }
}
