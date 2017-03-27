package com.tokopedia.discovery.intermediary.di;

import android.content.Context;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.discovery.intermediary.data.mapper.IntermediaryCategoryMapper;
import com.tokopedia.discovery.intermediary.data.repository.IntermediaryRepositoryImpl;
import com.tokopedia.discovery.intermediary.data.source.IntermediaryDataSource;
import com.tokopedia.discovery.intermediary.domain.IntermediaryRepository;
import com.tokopedia.discovery.intermediary.domain.interactor.GetIntermediaryCategoryUseCase;
import com.tokopedia.discovery.intermediary.view.IntermediaryPresenter;

/**
 * Created by alifa on 3/27/17.
 */

public class IntermediaryDependencyInjector {

    public static IntermediaryPresenter getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        HadesService service = new HadesService();
        HadesApi api = service.getApi();
        IntermediaryCategoryMapper mapper = new IntermediaryCategoryMapper();
        IntermediaryDataSource dataSource = new IntermediaryDataSource(context,api,mapper);

        IntermediaryRepository repository = new IntermediaryRepositoryImpl(dataSource);

        GetIntermediaryCategoryUseCase getIntermediaryCategoryUseCase = new GetIntermediaryCategoryUseCase(
                threadExecutor, postExecutionThread, repository);

        return  new IntermediaryPresenter(getIntermediaryCategoryUseCase);

    }
}
