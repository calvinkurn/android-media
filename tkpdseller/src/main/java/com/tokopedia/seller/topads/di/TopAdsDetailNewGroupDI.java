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
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateExistingGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCreateNewGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewGroupPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailNewGroupDI {

    public static TopAdsDetailNewGroupPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService();
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();

        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi, topAdsSearchGroupMapper, topAdsDetailGroupMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);

        TopAdsGetDetailGroupUseCase topAdsGetDetailGroupUseCase = new TopAdsGetDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailGroupUseCase topAdsSaveDetailGroupUseCase = new TopAdsSaveDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);

        TopAdsCreateNewGroupUseCase topAdsCreateNewGroupUseCase = new TopAdsCreateNewGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsCreateExistingGroupUseCase topAdsCreateExistingGroupUseCase = new TopAdsCreateExistingGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);

        return new TopAdsDetailNewGroupPresenterImpl(topAdsCreateNewGroupUseCase, topAdsCreateExistingGroupUseCase, topAdsGetDetailGroupUseCase, topAdsSaveDetailGroupUseCase);
    }
}

