package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.topads.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailEditGroupDI {

    public static TopAdsDetailEditGroupPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService();
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();

        TopAdsGroupAdFactory topAdsShopAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi, topAdsSearchGroupMapper, topAdsDetailGroupMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsShopAdFactory);

        TopAdsGetDetailGroupUseCase getDetailGroupUseCase = new TopAdsGetDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailGroupUseCase saveDetailGroupUseCase = new TopAdsSaveDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        return new TopAdsDetailEditGroupPresenterImpl(getDetailGroupUseCase, saveDetailGroupUseCase);
    }
}
