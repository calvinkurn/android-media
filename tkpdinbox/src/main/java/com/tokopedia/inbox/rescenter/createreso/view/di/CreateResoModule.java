package com.tokopedia.inbox.rescenter.createreso.view.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoWithoutAttachmentMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithAttachmentUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateSubmitUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.CreateValidateUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.UploadUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach.UploadVideoUseCase;

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
    CreateResoWithoutAttachmentMapper provideCreateResoStep1Mapper() {
        return new CreateResoWithoutAttachmentMapper();
    }

    @CreateResoScope
    @Provides
    CreateResolutionFactory provideProductProblemFactory(@ApplicationContext Context context,
                                                         CreateValidateMapper createValidateMapper,
                                                         GenerateHostMapper generateHostMapper,
                                                         UploadMapper uploadMapper,
                                                         CreateSubmitMapper createSubmitMapper,
                                                         ResolutionApi resolutionApi,
                                                         ResCenterActService resCenterActService,
                                                         UploadVideoMapper uploadVideoMapper
    ) {
        return new CreateResolutionFactory(context,
                createValidateMapper,
                generateHostMapper,
                uploadMapper,
                createSubmitMapper,
                resolutionApi,
                resCenterActService,
                uploadVideoMapper
        );
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
                                                                           CreateSubmitUseCase createSubmitUseCase,
                                                                           UploadVideoUseCase uploadVideoUseCase) {
        return new CreateResoWithAttachmentUseCase(threadExecutor,
                postExecutionThread,
                createValidateUseCase,
                generateHostUseCase,
                uploadUseCase,
                createSubmitUseCase,
                uploadVideoUseCase
        );
    }

    @CreateResoScope
    @Provides
    UploadVideoUseCase provideUploadVideoUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 GenerateHostUploadRepository generateHostUploadRepository) {
        return new UploadVideoUseCase(threadExecutor,
                postExecutionThread,
                generateHostUploadRepository);
    }

    @CreateResoScope
    @Provides
    UploadVideoMapper provideUploadVideoMapper() {
        return new UploadVideoMapper();
    }

    @CreateResoScope
    @Provides
    ResCenterActService provideResCenterActService() {
        return new ResCenterActService();
    }


}
