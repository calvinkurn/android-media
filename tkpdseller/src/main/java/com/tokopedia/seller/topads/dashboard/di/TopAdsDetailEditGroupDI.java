package com.tokopedia.seller.topads.dashboard.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsGroupAdFactory;
import com.tokopedia.seller.topads.dashboard.data.mapper.SearchProductEOFMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.seller.topads.dashboard.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsGroupAdsRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsSearchProductRepositoryImpl;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.CloudTopAdsSearchProductDataSource;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.TopAdsManagementService;
import com.tokopedia.seller.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsGroupAdsRepository;
import com.tokopedia.seller.topads.dashboard.domain.TopAdsSearchProductRepository;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsGetDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsProductListUseCase;
import com.tokopedia.seller.topads.dashboard.domain.interactor.TopAdsSaveDetailGroupUseCase;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenterImpl;

/**
 * Created by zulfikarrahman on 2/21/17.
 */

public class TopAdsDetailEditGroupDI {

    public static TopAdsDetailEditGroupPresenter createPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        TopAdsManagementService topAdsManagementService = new TopAdsManagementService(new SessionHandler(context).getAccessToken(context));
        TopAdsManagementApi topAdsManagementApi = topAdsManagementService.getApi();

        TopAdsSearchGroupMapper topAdsSearchGroupMapper = new TopAdsSearchGroupMapper();
        TopAdsDetailGroupMapper topAdsDetailGroupMapper = new TopAdsDetailGroupMapper();
        TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper = new TopAdsDetailGroupDomainMapper();
        SearchProductEOFMapper searchProductEOFMapper = new SearchProductEOFMapper();

        CloudTopAdsSearchProductDataSource cloudTopAdsSearchProductDataSource = new CloudTopAdsSearchProductDataSource(context, topAdsManagementService,searchProductEOFMapper);

        TopAdsGroupAdFactory topAdsGroupAdFactory = new TopAdsGroupAdFactory(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper, topAdsDetailGroupDomainMapper);

        TopAdsGroupAdsRepository topAdsGroupAdsRepository = new TopAdsGroupAdsRepositoryImpl(topAdsGroupAdFactory);
        TopAdsSearchProductRepository topAdsSearchProductRepository = new TopAdsSearchProductRepositoryImpl(context,cloudTopAdsSearchProductDataSource);

        TopAdsGetDetailGroupUseCase getDetailGroupUseCase = new TopAdsGetDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsSaveDetailGroupUseCase saveDetailGroupUseCase = new TopAdsSaveDetailGroupUseCase(threadExecutor, postExecutionThread, topAdsGroupAdsRepository);
        TopAdsProductListUseCase topAdsProductListUseCase = new TopAdsProductListUseCase(threadExecutor,postExecutionThread, topAdsSearchProductRepository);
        return new TopAdsDetailEditGroupPresenterImpl(getDetailGroupUseCase, saveDetailGroupUseCase, topAdsProductListUseCase);
    }
}
