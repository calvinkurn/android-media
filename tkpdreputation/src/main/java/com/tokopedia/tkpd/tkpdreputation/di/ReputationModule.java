package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.ReputationRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.ReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReviewValidateUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;

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
            GlobalCacheManager globalCacheManager) {
        return new ReputationFactory(reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper,
                sendReviewValidateMapper, globalCacheManager);
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
                             SendReviewValidateUseCase sendReviewValidateUseCase) {
        return new SendReviewUseCase(
                threadExecutor,
                postExecutionThread,
                sendReviewValidateUseCase);
    }
}
