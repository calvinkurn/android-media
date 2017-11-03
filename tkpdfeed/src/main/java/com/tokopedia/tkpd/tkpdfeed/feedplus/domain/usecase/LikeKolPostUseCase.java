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

    FeedRepository feedRepository;

    public LikeKolPostUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<LikeKolDomain> createObservable(RequestParams requestParams) {
        return feedRepository.likeKolPost(requestParams);
    }


    public static RequestParams getParam(int id) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ID, id);
        return params;
    }
}
