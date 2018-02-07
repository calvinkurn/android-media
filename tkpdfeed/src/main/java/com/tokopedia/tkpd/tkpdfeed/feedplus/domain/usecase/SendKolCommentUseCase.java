package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.SendKolCommentDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class SendKolCommentUseCase extends UseCase<SendKolCommentDomain> {

    public static final String PARAM_ID = "id";
    public static final String PARAM_COMMENT = "comment";

    FeedRepository feedRepository;

    public SendKolCommentUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<SendKolCommentDomain> createObservable(RequestParams requestParams) {
        return feedRepository.sendKolComment(requestParams);
    }

    public static RequestParams getParam(int id, String comment) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putString(PARAM_COMMENT, comment);
        return params;
    }
}
