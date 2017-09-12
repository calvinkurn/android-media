package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.GetSendReviewFormUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewSubmitUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SendReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SetReviewFormCacheUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.SkipReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.factory.ImageUploadFactory;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.GenerateHostMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.mapper.UploadImageMapper;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepository;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.data.repository.ImageUploadRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.GenerateHostUseCase;
import com.tokopedia.tkpd.tkpdreputation.uploadimage.domain.interactor.UploadImageUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 8/11/17.
 */

@Module
public class ReputationModule {

    @ReputationScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @ReputationScope
    @Provides
    GetFirstTimeInboxReputationUseCase
    provideGetFirstTimeInboxReputationUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              ReputationRepository reputationRepository) {
        return new GetFirstTimeInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    GetInboxReputationUseCase
    provideGetInboxReputationUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        return new GetInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    ReputationRepository provideReputationRepository(ReputationFactory reputationFactory) {
        return new ReputationRepositoryImpl(reputationFactory);
    }

    @ReputationScope
    @Provides
    ReputationFactory provideReputationFactory(
            ReputationService reputationService,
            InboxReputationMapper inboxReputationMapper,
            InboxReputationDetailMapper inboxReputationDetailMapper,
            SendSmileyReputationMapper sendSmileyReputationMapper,
            SendReviewValidateMapper sendReviewValidateMapper,
            SendReviewSubmitMapper sendReviewSubmitMapper,
            SkipReviewMapper skipReviewMapper,
            GlobalCacheManager globalCacheManager) {
        return new ReputationFactory(reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper,
                sendReviewValidateMapper, sendReviewSubmitMapper,
                skipReviewMapper,
                globalCacheManager);
    }

    @ReputationScope
    @Provides
    InboxReputationMapper provideInboxReputationMapper() {
        return new InboxReputationMapper();
    }

    @ReputationScope
    @Provides
    ReputationService provideReputationService() {
        return new ReputationService();
    }

    @ReputationScope
    @Provides
    InboxReputationDetailMapper provideInboxReputationDetailMapper() {
        return new InboxReputationDetailMapper();
    }

    @ReputationScope
    @Provides
    GetInboxReputationDetailUseCase
    provideGetInboxReputationDetailUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           ReputationRepository reputationRepository) {
        return new GetInboxReputationDetailUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    SendSmileyReputationMapper provideSendSmileyReputationMapper() {
        return new SendSmileyReputationMapper();
    }

    @ReputationScope
    @Provides
    SendSmileyReputationUseCase
    provideSendSmileyReputationUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReputationRepository reputationRepository) {
        return new SendSmileyReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }


    @ReputationScope
    @Provides
    SendReviewValidateMapper provideSendReviewValidateMapper() {
        return new SendReviewValidateMapper();
    }


    @ReputationScope
    @Provides
    SendReviewValidateUseCase
    provideSendReviewValidateUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReputationRepository reputationRepository) {
        return new SendReviewValidateUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    SendReviewUseCase
    provideSendReviewUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SendReviewValidateUseCase sendReviewValidateUseCase,
                             GenerateHostUseCase generateHostUseCase,
                             UploadImageUseCase uploadImageUseCase,
                             SendReviewSubmitUseCase sendReviewSubmitUseCase
    ) {
        return new SendReviewUseCase(
                threadExecutor,
                postExecutionThread,
                sendReviewValidateUseCase,
                generateHostUseCase,
                uploadImageUseCase,
                sendReviewSubmitUseCase
        );
    }

    @ReputationScope
    @Provides
    GetSendReviewFormUseCase
    provideGetSendReviewFormUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    GlobalCacheManager globalCacheManager) {
        return new GetSendReviewFormUseCase(
                threadExecutor,
                postExecutionThread,
                globalCacheManager);
    }

    @ReputationScope
    @Provides
    SetReviewFormCacheUseCase
    provideSetReviewFormCacheUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     GlobalCacheManager globalCacheManager) {
        return new SetReviewFormCacheUseCase(
                threadExecutor,
                postExecutionThread,
                globalCacheManager);
    }

    @ReputationScope
    @Provides
    UploadImageUseCase
    provideUploadImageUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              ImageUploadRepository imageUploadRepository) {
        return new UploadImageUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @ReputationScope
    @Provides
    GenerateHostUseCase
    provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               ImageUploadRepository imageUploadRepository) {
        return new GenerateHostUseCase(
                threadExecutor,
                postExecutionThread,
                imageUploadRepository);
    }

    @ReputationScope
    @Provides
    ImageUploadRepository
    provideImageUploadRepository(ImageUploadFactory imageUploadFactory) {
        return new ImageUploadRepositoryImpl(imageUploadFactory);
    }

    @ReputationScope
    @Provides
    ImageUploadFactory
    provideImageUploadFactory(GenerateHostActService generateHostActService,
                              UploadImageService uploadImageService,
                              GenerateHostMapper generateHostMapper,
                              UploadImageMapper uploadImageMapper) {
        return new ImageUploadFactory(generateHostActService,
                uploadImageService,
                generateHostMapper,
                uploadImageMapper);
    }

    @ReputationScope
    @Provides
    GenerateHostActService
    provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @ReputationScope
    @Provides
    UploadImageService
    provideUploadImageService() {
        return new UploadImageService();
    }

    @ReputationScope
    @Provides
    GenerateHostMapper
    provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @ReputationScope
    @Provides
    UploadImageMapper
    provideUploadImageMapper() {
        return new UploadImageMapper();
    }

    @ReputationScope
    @Provides
    SendReviewSubmitUseCase
    provideSendReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ReputationRepository reputationRepository) {
        return new SendReviewSubmitUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @ReputationScope
    @Provides
    SendReviewSubmitMapper provideSendReviewSubmitMapper() {
        return new SendReviewSubmitMapper();
    }


    @ReputationScope
    @Provides
    SkipReviewUseCase provideSkipReviewUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               ReputationRepository reputationRepository) {
        return new SkipReviewUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }


    @ReputationScope
    @Provides
    SkipReviewMapper provideSkipReviewMapper() {
        return new SkipReviewMapper();
    }
}
