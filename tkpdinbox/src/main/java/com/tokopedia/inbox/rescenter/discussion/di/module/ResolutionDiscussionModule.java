package com.tokopedia.inbox.rescenter.discussion.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.di.scope.ResolutionDiscussionScope;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.CreatePictureUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GenerateHostUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.GetResCenterDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.LoadMoreDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionSubmitUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.ReplyDiscussionValidationUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.discussion.domain.interactor.UploadImageUseCase;
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
            SendReplyDiscussionParam sendReplyDiscussionParam) {
        return new ResCenterDiscussionPresenterImpl(
                viewListener,
                getDiscussionUseCase,
                loadMoreDiscussionUseCase,
                sendDiscussionUseCase,
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
                                                       UploadVideoUseCase uploadVideoUseCase,
                                                       CreatePictureUseCase createPictureUseCase,
                                                       ReplyDiscussionSubmitUseCase replyDiscussionSubmitUseCase) {
        return new SendDiscussionUseCase(
                threadExecutor,
                postExecutionThread,
                generateHostUseCase,
                replyDiscussionValidationUseCase,
                uploadImageUseCase,
                uploadVideoUseCase,
                createPictureUseCase,
                replyDiscussionSubmitUseCase
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
