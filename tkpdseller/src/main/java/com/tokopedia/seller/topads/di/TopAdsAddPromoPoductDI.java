package com.tokopedia.seller.topads.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.seller.topads.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.seller.topads.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsAddPromoPoductDI {
    public static TopAdsManageGroupPromoPresenter createPresenter(Context context) {
        // Gson gson = new Gson();

        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService();
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();
        TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper = new TopAdsDetailGroupDomainMapper();


        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper, topAdsDetailGroupDomainMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);

        TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase = new TopAdsSearchGroupAdsNameUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase = new TopAdsCheckExistGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        return new TopAdsManageGroupPromoPresenterImpl(topAdsSearchGroupAdsNameUseCase, topAdsCheckExistGroupUseCase);
    }
}
