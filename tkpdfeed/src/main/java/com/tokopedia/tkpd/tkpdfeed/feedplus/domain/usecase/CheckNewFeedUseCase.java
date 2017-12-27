package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;

import rx.Observable;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckNewFeedUseCase extends UseCase<CheckFeedDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_CURSOR = "PARAM_CURSOR";
    private FeedRepository feedRepository;

    public CheckNewFeedUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<CheckFeedDomain> createObservable(RequestParams requestParams) {
        return feedRepository.checkNewFeed(requestParams);
    }

    public static RequestParams getParam(SessionHandler sessionHandler, String firstCursor) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, sessionHandler.getLoginID());
        params.putString(PARAM_CURSOR, firstCursor);
        return params;
    }
}
