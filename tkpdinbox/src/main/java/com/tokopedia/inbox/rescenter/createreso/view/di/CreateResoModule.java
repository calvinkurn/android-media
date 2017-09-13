package com.tokopedia.inbox.rescenter.createreso.view.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResCenterActApi;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.apiservices.upload.apis.UploadImageActApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep1Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoStep2Mapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep1Repository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep1RepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep2Repository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoStep2RepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep1UseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoStep2UseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithAttachmentUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateSubmitUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateValidateUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.UploadUseCase;


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
    CreateValidateMapper provideCreateValidateMapper() {
        return new CreateValidateMapper();
    }

    @CreateResoScope
    @Provides
    GenerateHostMapper provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @CreateResoScope
    @Provides
    UploadMapper provideUploadMapper() {
        return new UploadMapper();
    }

    @CreateResoScope
    @Provides
    CreateSubmitMapper provideCreateSubmitMapper() {
        return new CreateSubmitMapper();
    }

    @CreateResoScope
    @Provides
    SolutionMapper provideSolutionMapper() {
        return new SolutionMapper(new Gson());
    }

    @CreateResoScope
    @Provides
    CreateResoStep1Mapper provideCreateResoStep1Mapper() {
        return new CreateResoStep1Mapper();
    }

    @CreateResoScope
    @Provides
    CreateResoStep2Mapper provideCreateResoStep2Mapper() {
        return new CreateResoStep2Mapper();
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
                                                         CreateResoStep1Mapper createResoStep1Mapper,
                                                         CreateResoStep2Mapper createResoStep2Mapper,
                                                         CreateValidateMapper createValidateMapper,
                                                         GenerateHostMapper generateHostMapper,
                                                         UploadMapper uploadMapper,
                                                         CreateSubmitMapper createSubmitMapper,
                                                         ResolutionApi resolutionApi) {
        return new CreateResolutionFactory(context,
                getProductProblemMapper,
                solutionMapper,
                createResoStep1Mapper,
                createResoStep2Mapper,
                createValidateMapper,
                generateHostMapper,
                uploadMapper,
                createSubmitMapper,
                resolutionApi);
    }

    @CreateResoScope
    @Provides
    CreateResoStep1Repository provideCreateResoStep1Repository(CreateResolutionFactory createResolutionFactory) {
        return new CreateResoStep1RepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    CreateResoStep2Repository provideCreateResoStep2Repository(CreateResolutionFactory createResolutionFactory) {
        return new CreateResoStep2RepositoryImpl(createResolutionFactory);
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
    CreateValidateSubmitRepository provideCreateValidateSubmitRepository(CreateResolutionFactory createResolutionFactory) {
        return new CreateValidateSubmitRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    GenerateHostUploadRepository provideGenerateHostUploadRepository(CreateResolutionFactory createResolutionFactory) {
        return new GenerateHostUploadRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    CreateValidateUseCase provideCreateValidateUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       CreateValidateSubmitRepository createValidateSubmitRepository) {
        return new CreateValidateUseCase(threadExecutor,
                postExecutionThread,
                createValidateSubmitRepository);
    }

    @CreateResoScope
    @Provides
    GenerateHostUseCase provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   GenerateHostUploadRepository generateHostUploadRepository) {
        return new GenerateHostUseCase(threadExecutor,
                postExecutionThread,
                generateHostUploadRepository);
    }

    @CreateResoScope
    @Provides
    UploadUseCase provideUploadUseCase(ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             GenerateHostUploadRepository generateHostUploadRepository) {
        return new UploadUseCase(threadExecutor,
                postExecutionThread,
                generateHostUploadRepository);
    }

    @CreateResoScope
    @Provides
    CreateSubmitUseCase provideCreateSubmitUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     CreateValidateSubmitRepository createValidateSubmitRepository) {
        return new CreateSubmitUseCase(threadExecutor,
                postExecutionThread,
                createValidateSubmitRepository);
    }

    @CreateResoScope
    @Provides
    CreateResoWithAttachmentUseCase provideCreateResoWithAttachmentUseCase(ThreadExecutor threadExecutor,
                                                                           PostExecutionThread postExecutionThread,
                                                                           CreateValidateUseCase createValidateUseCase,
                                                                           GenerateHostUseCase generateHostUseCase,
                                                                           UploadUseCase uploadUseCase,
                                                                           CreateSubmitUseCase createSubmitUseCase) {
        return new CreateResoWithAttachmentUseCase(threadExecutor,
                postExecutionThread,
                createValidateUseCase,
                generateHostUseCase,
                uploadUseCase,
                createSubmitUseCase);
    }

    @CreateResoScope
    @Provides
    CreateResoStep1UseCase provideCreateResoStep1UseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         CreateResoStep1Repository createResoStep1Repository) {
        return new CreateResoStep1UseCase(threadExecutor,
                postExecutionThread,
                createResoStep1Repository);
    }

    @CreateResoScope
    @Provides
    CreateResoStep2UseCase provideCreateResoStep2UseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         CreateResoStep2Repository createResoStep2Repository) {
        return new CreateResoStep2UseCase(threadExecutor,
                postExecutionThread,
                createResoStep2Repository);
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
