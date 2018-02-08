package com.tokopedia.discovery.intermediary.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.data.repository.IntermediaryRepositoryImpl;
import com.tokopedia.discovery.intermediary.data.source.IntermediaryDataSource;
import com.tokopedia.discovery.intermediary.domain.IntermediaryRepository;
import com.tokopedia.discovery.intermediary.domain.interactor.GetCategoryHeaderUseCase;
import com.tokopedia.discovery.intermediary.domain.interactor.GetIntermediaryCategoryUseCase;
import com.tokopedia.discovery.intermediary.view.IntermediaryPresenter;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryDependencyInjector {

    public static IntermediaryPresenter getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        HadesService hadesService = new HadesService();
        HadesApi hadesApi = hadesService.getApi();
        AceSearchService aceSearchService = new AceSearchService();
        SearchApi searchApi = aceSearchService.getApi();
        MojitoService mojitoService = new MojitoService();
        MojitoApi mojitoApi = mojitoService.getApi();
        IntermediaryCategoryMapper mapper = new IntermediaryCategoryMapper();
        IntermediaryDataSource dataSource = new IntermediaryDataSource(context,hadesApi,
                searchApi,mojitoApi,mapper);

        IntermediaryRepository repository = new IntermediaryRepositoryImpl(dataSource);

        GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase = new GetIntermediaryCategoryUseCase(
                threadExecutor, postExecutionThread, repository);
        GetCategoryHeaderUseCase getCategoryHeaderUseCase = new GetCategoryHeaderUseCase(threadExecutor, postExecutionThread, repository);

        return  new IntermediaryPresenter(getIntermediaryCategoryUseCase, getCategoryHeaderUseCase);

    }
}
