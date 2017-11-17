package com.tokopedia.inbox.rescenter.createreso.view.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.rescenter.ResCenterActService;
import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.createreso.data.factory.CreateResolutionFactory;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.AppealSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateResoWithoutAttachmentMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateSubmitMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.CreateValidateMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditAppealResolutionResponseMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.EditSolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GenerateHostMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.GetProductProblemMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.SolutionMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadMapper;
import com.tokopedia.inbox.rescenter.createreso.data.mapper.UploadVideoMapper;
import com.tokopedia.inbox.rescenter.createreso.data.repository.AppealSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.AppealSolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoWithoutAttachmentRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateResoWithoutAttachmentRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.CreateValidateSubmitRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.EditSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.EditSolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostAppealSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostAppealSolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostEditSolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.PostEditSolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.ProductProblemRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepository;
import com.tokopedia.inbox.rescenter.createreso.data.repository.SolutionRepositoryImpl;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithAttachmentUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.CreateResoWithoutAttachmentUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetAppealSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetEditSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetProductProblemUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.GetSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostAppealSolutionUseCase;
import com.tokopedia.inbox.rescenter.createreso.domain.usecase.PostEditSolutionUseCase;
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
    EditAppealResolutionResponseMapper provideEditAppealResolutionResponseMapper() {
        return new EditAppealResolutionResponseMapper();
    }

    @CreateResoScope
    @Provides
    AppealSolutionMapper provideAppealSolutionMapper() {
        return new AppealSolutionMapper();
    }


    @CreateResoScope
    @Provides
    EditSolutionMapper provideEditSolutionMapper() {
        return new EditSolutionMapper();
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
        return new SolutionMapper();
    }

    @CreateResoScope
    @Provides
    CreateResoWithoutAttachmentMapper provideCreateResoStep1Mapper() {
        return new CreateResoWithoutAttachmentMapper();
    }

    @CreateResoScope
    @Provides
    GetProductProblemMapper provideGetProductProblemMapper() {
        return new GetProductProblemMapper();
    }

    @CreateResoScope
    @Provides
    CreateResolutionFactory provideProductProblemFactory(@ApplicationContext Context context,
                                                         GetProductProblemMapper getProductProblemMapper,
                                                         SolutionMapper solutionMapper,
                                                         CreateResoWithoutAttachmentMapper createResoWithoutAttachmentMapper,
                                                         CreateValidateMapper createValidateMapper,
                                                         GenerateHostMapper generateHostMapper,
                                                         UploadMapper uploadMapper,
                                                         CreateSubmitMapper createSubmitMapper,
                                                         ResolutionApi resolutionApi,
                                                         ResCenterActService resCenterActService,
                                                         UploadVideoMapper uploadVideoMapper,
                                                         EditSolutionMapper editSolutionMapper,
                                                         AppealSolutionMapper appealSolutionMapper,
                                                         EditAppealResolutionResponseMapper editAppealResolutionResponseMapper) {
        return new CreateResolutionFactory(context,
                getProductProblemMapper,
                solutionMapper,
                createResoWithoutAttachmentMapper,
                createValidateMapper,
                generateHostMapper,
                uploadMapper,
                createSubmitMapper,
                resolutionApi,
                resCenterActService,
                uploadVideoMapper,
                editSolutionMapper,
                appealSolutionMapper,
                editAppealResolutionResponseMapper
        );
    }

    @CreateResoScope
    @Provides
    PostEditSolutionRepository providePostEditSolutionRepository(CreateResolutionFactory createResolutionFactory) {
        return new PostEditSolutionRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    PostAppealSolutionRepository providePostAppealSolutionRepository(CreateResolutionFactory createResolutionFactory) {
        return new PostAppealSolutionRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    EditSolutionRepository provideEditSolutionRepository(CreateResolutionFactory createResolutionFactory) {
        return new EditSolutionRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    AppealSolutionRepository provideAppealSolutionRepository(CreateResolutionFactory createResolutionFactory) {
        return new AppealSolutionRepositoryImpl(createResolutionFactory);
    }

    @CreateResoScope
    @Provides
    CreateResoWithoutAttachmentRepository provideCreateResoStep1Repository(CreateResolutionFactory createResolutionFactory) {
        return new CreateResoWithoutAttachmentRepositoryImpl(createResolutionFactory);
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
    GetEditSolutionUseCase provideGetEditSolutionUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         EditSolutionRepository editSolutionRepository) {
        return new GetEditSolutionUseCase(threadExecutor,
                postExecutionThread,
                editSolutionRepository);
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
    CreateResoWithoutAttachmentUseCase provideCreateResoStep1UseCase(ThreadExecutor threadExecutor,
                                                                     PostExecutionThread postExecutionThread,
                                                                     CreateResoWithoutAttachmentRepository createResoWithoutAttachmentRepository) {
        return new CreateResoWithoutAttachmentUseCase(threadExecutor,
                postExecutionThread,
                createResoWithoutAttachmentRepository);
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
    GetAppealSolutionUseCase provideGetAppealSolutionUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             AppealSolutionRepository appealSolutionRepository) {
        return new GetAppealSolutionUseCase(threadExecutor,
                postExecutionThread,
                appealSolutionRepository);
    }

    @CreateResoScope
    @Provides
    PostEditSolutionUseCase providePostEditSolutionUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           PostEditSolutionRepository postEditSolutionRepository) {
        return new PostEditSolutionUseCase(threadExecutor,
                postExecutionThread,
                postEditSolutionRepository);
    }

    @CreateResoScope
    @Provides
    PostAppealSolutionUseCase providePostAppealSolutionUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               PostAppealSolutionRepository postAppealSolutionRepository) {
        return new PostAppealSolutionUseCase(threadExecutor,
                postExecutionThread,
                postAppealSolutionRepository);
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
