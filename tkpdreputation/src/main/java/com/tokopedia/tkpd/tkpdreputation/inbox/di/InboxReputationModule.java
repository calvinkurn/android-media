package com.tokopedia.tkpd.tkpdreputation.inbox.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.UploadImageService;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.apiservices.user.ReputationService;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.GetLikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.data.mapper.LikeDislikeMapper;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.ReportReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.factory.InboxReputationFactory;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.DeleteReviewResponseMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.FaveShopMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationDetailMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.InboxReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReplyReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ReportReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewSubmitMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendReviewValidateMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SendSmileyReputationMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.ShopFavoritedMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper.SkipReviewMapper;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.InboxReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.InboxReputationRepositoryImpl;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetCacheInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.CheckShopFavoritedUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.GetReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendReplyReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inboxdetail.SendSmileyReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewSubmitUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.sendreview.EditReviewValidateUseCase;
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
public class InboxReputationModule {

    @InboxReputationScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @InboxReputationScope
    @Provides
    GetFirstTimeInboxReputationUseCase
    provideGetFirstTimeInboxReputationUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              GetInboxReputationUseCase getInboxReputationUseCase,
                                              GetCacheInboxReputationUseCase
                                                      getCacheInboxReputationUseCase,
                                              InboxReputationRepository reputationRepository) {
        return new GetFirstTimeInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                getInboxReputationUseCase,
                getCacheInboxReputationUseCase,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    GetInboxReputationUseCase
    provideGetInboxReputationUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     InboxReputationRepository reputationRepository) {
        return new GetInboxReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    InboxReputationRepository provideReputationRepository(InboxReputationFactory reputationFactory) {
        return new InboxReputationRepositoryImpl(reputationFactory);
    }

    @InboxReputationScope
    @Provides
    InboxReputationFactory provideReputationFactory(
            TomeService tomeService,
            ReputationService reputationService,
            InboxReputationMapper inboxReputationMapper,
            InboxReputationDetailMapper inboxReputationDetailMapper,
            SendSmileyReputationMapper sendSmileyReputationMapper,
            SendReviewValidateMapper sendReviewValidateMapper,
            SendReviewSubmitMapper sendReviewSubmitMapper,
            SkipReviewMapper skipReviewMapper,
            ShopFavoritedMapper shopFavoritedMapper,
            ReportReviewMapper reportReviewMapper,
            GlobalCacheManager globalCacheManager,
            FaveShopActService faveShopActService,
            FaveShopMapper faveShopMapper,
            DeleteReviewResponseMapper deleteReviewResponseMapper,
            ReplyReviewMapper replyReviewMapper,
            GetLikeDislikeMapper getLikeDislikeMapper,
            LikeDislikeMapper likeDislikeMapper) {
        return new InboxReputationFactory(tomeService, reputationService, inboxReputationMapper,
                inboxReputationDetailMapper, sendSmileyReputationMapper,
                sendReviewValidateMapper, sendReviewSubmitMapper,
                skipReviewMapper, reportReviewMapper,
                shopFavoritedMapper,
                globalCacheManager,
                faveShopActService,
                faveShopMapper,
                deleteReviewResponseMapper,
                replyReviewMapper,
                getLikeDislikeMapper,
                likeDislikeMapper);
    }


    @InboxReputationScope
    @Provides
    InboxReputationMapper provideInboxReputationMapper() {
        return new InboxReputationMapper();
    }

    @InboxReputationScope
    @Provides
    ReputationService provideReputationService() {
        return new ReputationService();
    }

    @InboxReputationScope
    @Provides
    InboxReputationDetailMapper provideInboxReputationDetailMapper() {
        return new InboxReputationDetailMapper();
    }

    @InboxReputationScope
    @Provides
    GetReviewUseCase
    provideGetReviewUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            InboxReputationRepository reputationRepository) {
        return new GetReviewUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    GetInboxReputationDetailUseCase
    provideGetInboxReputationDetailUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           GetInboxReputationUseCase getInboxReputationUseCase,
                                           GetReviewUseCase getReviewUseCase,
                                           GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase) {
        return new GetInboxReputationDetailUseCase(
                threadExecutor,
                postExecutionThread,
                getInboxReputationUseCase,
                getReviewUseCase,
                getLikeDislikeReviewUseCase);
    }

    @InboxReputationScope
    @Provides
    SendSmileyReputationMapper provideSendSmileyReputationMapper() {
        return new SendSmileyReputationMapper();
    }

    @InboxReputationScope
    @Provides
    SendSmileyReputationUseCase
    provideSendSmileyReputationUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       InboxReputationRepository reputationRepository) {
        return new SendSmileyReputationUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }


    @InboxReputationScope
    @Provides
    SendReviewValidateMapper provideSendReviewValidateMapper() {
        return new SendReviewValidateMapper();
    }


    @InboxReputationScope
    @Provides
    SendReviewValidateUseCase
    provideSendReviewValidateUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     InboxReputationRepository reputationRepository) {
        return new SendReviewValidateUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
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

    @InboxReputationScope
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

    @InboxReputationScope
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

    @InboxReputationScope
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

    @InboxReputationScope
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

    @InboxReputationScope
    @Provides
    ImageUploadRepository
    provideImageUploadRepository(ImageUploadFactory imageUploadFactory) {
        return new ImageUploadRepositoryImpl(imageUploadFactory);
    }

    @InboxReputationScope
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

    @InboxReputationScope
    @Provides
    GenerateHostActService
    provideGenerateHostActService() {
        return new GenerateHostActService();
    }

    @InboxReputationScope
    @Provides
    UploadImageService
    provideUploadImageService() {
        return new UploadImageService();
    }

    @InboxReputationScope
    @Provides
    GenerateHostMapper
    provideGenerateHostMapper() {
        return new GenerateHostMapper();
    }

    @InboxReputationScope
    @Provides
    UploadImageMapper
    provideUploadImageMapper() {
        return new UploadImageMapper();
    }

    @InboxReputationScope
    @Provides
    SendReviewSubmitUseCase
    provideSendReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   InboxReputationRepository reputationRepository) {
        return new SendReviewSubmitUseCase(
                threadExecutor,
                postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    SendReviewSubmitMapper provideSendReviewSubmitMapper() {
        return new SendReviewSubmitMapper();
    }


    @InboxReputationScope
    @Provides
    SkipReviewUseCase provideSkipReviewUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               InboxReputationRepository reputationRepository) {
        return new SkipReviewUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }


    @InboxReputationScope
    @Provides
    SkipReviewMapper provideSkipReviewMapper() {
        return new SkipReviewMapper();
    }

    @InboxReputationScope
    @Provides
    EditReviewValidateUseCase provideEditReviewValidateUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               InboxReputationRepository reputationRepository) {
        return new EditReviewValidateUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @InboxReputationScope
    @Provides
    EditReviewSubmitUseCase provideEditReviewSubmitUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           InboxReputationRepository reputationRepository) {
        return new EditReviewSubmitUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @InboxReputationScope
    @Provides
    EditReviewUseCase provideEditReviewUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               EditReviewValidateUseCase editReviewValidateUseCase,
                                               GenerateHostUseCase generateHostUseCase,
                                               UploadImageUseCase uploadImageUseCase,
                                               EditReviewSubmitUseCase editReviewSubmitUseCase) {
        return new EditReviewUseCase(threadExecutor, postExecutionThread,
                editReviewValidateUseCase, generateHostUseCase,
                uploadImageUseCase, editReviewSubmitUseCase);
    }

    @InboxReputationScope
    @Provides
    ReportReviewMapper provideReportReviewMapper() {
        return new ReportReviewMapper();
    }

    @InboxReputationScope
    @Provides
    ReportReviewUseCase provideReportReviewUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   InboxReputationRepository reputationRepository) {
        return new ReportReviewUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @InboxReputationScope
    @Provides
    GetCacheInboxReputationUseCase provideGetCacheInboxReputationUseCase(ThreadExecutor threadExecutor,
                                                                         PostExecutionThread postExecutionThread,
                                                                         InboxReputationRepository reputationRepository) {
        return new GetCacheInboxReputationUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }


    @InboxReputationScope
    @Provides
    TomeService provideTomeService() {
        return new TomeService();
    }

    @InboxReputationScope
    @Provides
    ShopFavoritedMapper provideShopFavoritedMapper() {
        return new ShopFavoritedMapper();
    }

    @InboxReputationScope
    @Provides
    CheckShopFavoritedUseCase provideCheckShopFavoritedUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository
    ) {
        return new CheckShopFavoritedUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @InboxReputationScope
    @Provides
    FavoriteShopUseCase provideFavoriteShopUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository
    ) {
        return new FavoriteShopUseCase(threadExecutor, postExecutionThread, reputationRepository);
    }

    @InboxReputationScope
    @Provides
    FaveShopMapper provideFaveShopMapper() {
        return new FaveShopMapper();
    }

    @InboxReputationScope
    @Provides
    FaveShopActService provideFaveShopActService() {
        return new FaveShopActService();
    }

    @InboxReputationScope
    @Provides
    DeleteReviewResponseUseCase provideDeleteReviewResponseUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository) {
        return new DeleteReviewResponseUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    DeleteReviewResponseMapper provideDeleteReviewResponseMapper() {
        return new DeleteReviewResponseMapper();
    }

    @InboxReputationScope
    @Provides
    SendReplyReviewUseCase provideSendReplyReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository) {
        return new SendReplyReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    ReplyReviewMapper provideReplyReviewMapper() {
        return new ReplyReviewMapper();
    }

    @InboxReputationScope
    @Provides
    LikeDislikeReviewUseCase provideLikeDislikeReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository) {
        return new LikeDislikeReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }

    @InboxReputationScope
    @Provides
    GetLikeDislikeMapper provideGetLikeDislikeMapper() {
        return new GetLikeDislikeMapper();
    }

    @InboxReputationScope
    @Provides
    GetLikeDislikeReviewUseCase provideGetLikeDislikeReviewUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            InboxReputationRepository reputationRepository) {
        return new GetLikeDislikeReviewUseCase(threadExecutor, postExecutionThread,
                reputationRepository);
    }


    @InboxReputationScope
    @Provides
    LikeDislikeMapper provideLikeDislikeMapper() {
        return new LikeDislikeMapper();
    }

}
