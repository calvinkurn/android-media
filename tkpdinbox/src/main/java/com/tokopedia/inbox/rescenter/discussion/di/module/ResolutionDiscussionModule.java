package com.tokopedia.inbox.rescenter.discussion.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.di.scope.ResolutionDiscussionScope;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.CreatePictureUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GenerateHostV2UseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GetResCenterDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.LoadMoreDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.NewReplyDiscussionSubmitUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.NewReplyDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionSubmitUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.UploadImageUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.UploadImageV2UseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.UploadVideoUseCase;
import com.tokopedia.inbox.rescenter.discussion.view.listener.ResCenterDiscussionView;
import com.tokopedia.inbox.rescenter.discussion.view.presenter.ResCenterDiscussionPresenterImpl;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.SendReplyDiscussionParam;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 4/18/17.
 */

@ResolutionDiscussionScope
@Module
public class ResolutionDiscussionModule {

    private final ResCenterDiscussionView viewListener;

    public ResolutionDiscussionModule(ResCenterDiscussionView viewListener) {
        this.viewListener = viewListener;
    }

    @ResolutionDiscussionScope
    @Provides
    ResCenterDiscussionPresenterImpl provideResCenterDiscussionPresenterImpl(
            GetResCenterDiscussionUseCase getDiscussionUseCase,
            LoadMoreDiscussionUseCase loadMoreDiscussionUseCase,
            SendDiscussionUseCase sendDiscussionUseCase,
            SendDiscussionV2UseCase sendDiscussionV2UseCase,
            SendReplyDiscussionParam sendReplyDiscussionParam) {
        return new ResCenterDiscussionPresenterImpl(
                viewListener,
                getDiscussionUseCase,
                loadMoreDiscussionUseCase,
                sendDiscussionUseCase,
                sendDiscussionV2UseCase,
                sendReplyDiscussionParam
        );
    }

    @ResolutionDiscussionScope
    @Provides
    GetResCenterDiscussionUseCase provideGetResCenterDiscussionUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       ResCenterRepository resCenterRepository) {
        return new GetResCenterDiscussionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    LoadMoreDiscussionUseCase provideLoadMoreDiscussionUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               ResCenterRepository resCenterRepository) {
        return new LoadMoreDiscussionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    SendDiscussionUseCase provideSendDiscussionUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       GenerateHostUseCase generateHostUseCase,
                                                       ReplyDiscussionValidationUseCase replyDiscussionValidationUseCase,
                                                       UploadImageUseCase uploadImageUseCase,
                                                       CreatePictureUseCase createPictureUseCase,
                                                       ReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase) {
        return new SendDiscussionUseCase(
                threadExecutor,
                postExecutionThread,
                generateHostUseCase,
                replyDiscussionValidationUseCase,
                uploadImageUseCase,
                createPictureUseCase,
                replyDiscussionSubmitUseCase
        );
    }

    @ResolutionDiscussionScope
    @Provides
    SendDiscussionV2UseCase provideSendDiscussionV2UseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           GenerateHostV2UseCase generateHostUseCase,
                                                           NewReplyDiscussionUseCase replyDiscussionUseCase,
                                                           NewReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase,
                                                           UploadImageV2UseCase uploadImageUseCase,
                                                           UploadVideoUseCase uploadVideoUseCase) {
        return new SendDiscussionV2UseCase(
                threadExecutor,
                postExecutionThread,
                generateHostUseCase,
                replyDiscussionUseCase,
                replyDiscussionSubmitUseCase,
                uploadImageUseCase,
                uploadVideoUseCase
        );
    }

    @ResolutionDiscussionScope
    @Provides
    ReplyDiscussionValidationUseCase provideReplyDiscussionValidationUseCase(ThreadExecutor threadExecutor,
                                                                             PostExecutionThread postExecutionThread,
                                                                             ResCenterRepository resCenterRepository) {
        return new ReplyDiscussionValidationUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    NewReplyDiscussionUseCase provideNewReplyDiscussionUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               ResCenterRepository resCenterRepository) {
        return new NewReplyDiscussionUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    NewReplyDiscussionSubmitUseCase provideNewReplyDiscussionSubmitUseCase(ThreadExecutor threadExecutor,
                                                                        PostExecutionThread postExecutionThread,
                                                                        ResCenterRepository resCenterRepository) {
        return new NewReplyDiscussionSubmitUseCase(
                threadExecutor,
                postExecutionThread,
                resCenterRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    GenerateHostV2UseCase provideGenerateHostV2UseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   ResCenterRepository repository) {
        return new GenerateHostV2UseCase(
                threadExecutor,
                postExecutionThread,
                repository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    GenerateHostUseCase provideGenerateHostUseCase(ThreadExecutor threadExecutor,
                                                   PostExecutionThread postExecutionThread,
                                                   UploadImageRepository uploadImageRepository) {
        return new GenerateHostUseCase(
                threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    UploadImageUseCase provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageRepository uploadImageRepository) {
        return new UploadImageUseCase(
                threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    UploadImageV2UseCase provideUploadImageV2UseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     UploadImageRepository uploadImageRepository) {
        return new UploadImageV2UseCase(
                threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    UploadVideoUseCase provideUploadVideoUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 UploadImageRepository uploadImageRepository) {
        return new UploadVideoUseCase(threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    CreatePictureUseCase provideCreatePictureUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     UploadImageRepository uploadImageRepository) {
        return new CreatePictureUseCase(
                threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    ReplyDiscussionSubmitUseCase provideReplyDiscussionSubmitUseCase(ThreadExecutor threadExecutor,
                                                                     PostExecutionThread postExecutionThread,
                                                                     UploadImageRepository uploadImageRepository) {
        return new ReplyDiscussionSubmitUseCase(
                threadExecutor,
                postExecutionThread,
                uploadImageRepository
        );
    }

    @ResolutionDiscussionScope
    @Provides
    SendReplyDiscussionParam provideSendReplyDiscussionParam() {
        return new SendReplyDiscussionParam();
    }

}
