package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.LikeKolDomain;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class FollowKolPostUseCase extends UseCase<FollowKolDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_STATUS = "PARAM_STATUS";

    FeedRepository feedRepository;

    public FollowKolPostUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FollowKolDomain> createObservable(RequestParams requestParams) {
        return feedRepository.followKol(requestParams);
    }
}
