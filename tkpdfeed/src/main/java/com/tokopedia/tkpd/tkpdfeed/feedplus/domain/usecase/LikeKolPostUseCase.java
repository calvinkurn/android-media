package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class LikeKolPostUseCase  extends UseCase<LikeKolDomain> {

    public static final String PARAM_ID = "PARAM_ID";
    public static final String PARAM_ACTION = "PARAM_ACTION";

    public static final int ACTION_LIKE = 1;
    public static final int ACTION_UNLIKE = 0;

    FeedRepository feedRepository;

    public LikeKolPostUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<LikeKolDomain> createObservable(RequestParams requestParams) {
        return feedRepository.likeUnlikeKolPost(requestParams);
    }


    public static RequestParams getParam(int id, int action) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        params.putInt(PARAM_ACTION, action);
        return params;
    }
}
