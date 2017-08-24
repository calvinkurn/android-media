package com.tokopedia.inbox.rescenter.createreso.view.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;


import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by yoasfs on 11/08/17.
 */


@Module
public class CreateResoModule {

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    public CreateResoModule() {

    }


    @CreateResoScope
    @Provides
    ResolutionApi provideResolutionService(@ResolutionQualifier Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

    @CreateResoScope
    @Provides
    SolutionMapper provideSolutionMapper() {
        return new SolutionMapper(new Gson());
    }

    @CreateResoScope
    @Provides
    GetProductProblemMapper provideGetProductProblemMapper() {
        return new GetProductProblemMapper(new Gson());
    }

    @CreateResoScope
    @Provides
    CreateResolutionFactory provideProductProblemFactory(@ActivityContext Context context,
                                                         GetProductProblemMapper getProductProblemMapper,
                                                         SolutionMapper solutionMapper,
                                                         ResolutionApi resolutionApi) {
        return new CreateResolutionFactory(context, getProductProblemMapper, solutionMapper, resolutionApi);
    }

    @CreateResoScope
    @Provides
    SolutionRepository provideSolutionRepository(CreateResolutionFactory createResolutionFactory) {
        return new SolutionRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    ProductProblemRepository provideProductProblemRepository(CreateResolutionFactory createResolutionFactory) {
        return new ProductProblemRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    GetSolutionUseCase provideGetSolutionUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 SolutionRepository solutionRepository) {
        return new GetSolutionUseCase(threadExecutor,
                postExecutionThread,
                solutionRepository);
    }

    @CreateResoScope
    @Provides
    GetProductProblemUseCase provideGetProductProblemUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ProductProblemRepository productProblemRepository) {
        return new GetProductProblemUseCase(threadExecutor,
                postExecutionThread,
                productProblemRepository);
    }


}
