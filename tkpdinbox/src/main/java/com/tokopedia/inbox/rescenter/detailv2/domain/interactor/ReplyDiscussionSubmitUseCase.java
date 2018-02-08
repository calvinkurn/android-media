package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.replysubmit.ReplySubmitModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class ReplyDiscussionSubmitUseCase extends UseCase<ReplySubmitModel> {

    public static final String PARAM_FILE_UPLOADED = "file_uploaded";
    public static final String PARAM_POST_KEY = "post_key";
    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final UploadImageRepository uploadImageRepository;

    public ReplyDiscussionSubmitUseCase(ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread,
                                        UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<ReplySubmitModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.submitImage(requestParams.getParameters());
    }
}
