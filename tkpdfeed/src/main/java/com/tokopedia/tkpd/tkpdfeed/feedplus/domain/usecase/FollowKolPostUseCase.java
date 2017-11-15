package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolPostUseCase extends UseCase<FollowKolDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_ACTION = "PARAM_ACTION";
    public static final int PARAM_FOLLOW = 1;
    public static final int PARAM_UNFOLLOW = 0;
    public static final int SUCCESS_STATUS = 1;


    FeedRepository feedRepository;

    public FollowKolPostUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FollowKolDomain> createObservable(RequestParams requestParams) {
        return feedRepository.followUnfollowKol(requestParams);
    }

    public static RequestParams getParam(int userId, int status) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_USER_ID, userId);
        params.putInt(PARAM_ACTION, status);
        return params;
    }
}
