package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.DeleteKolCommentDomain;

import rx.Observable;

/**
 * @author by nisie on 11/10/17.
 */

public class DeleteKolCommentUseCase extends UseCase<DeleteKolCommentDomain> {

    public static final String PARAM_ID = "comment_id";

    FeedRepository feedRepository;

    public DeleteKolCommentUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<DeleteKolCommentDomain> createObservable(RequestParams requestParams) {
        return feedRepository.deleteKolComment(requestParams);
    }


    public static RequestParams getParam(int commentId) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, commentId);
        return params;
    }
}
