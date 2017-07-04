package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFeedsUseCase extends UseCase<FeedResult> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_CURSOR = "PARAM_CURSOR";
    protected FeedRepository feedRepository;

    public GetFeedsUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getFeedsFromCloud(requestParams);
    }

    public RequestParams getFeedPlusParam(SessionHandler sessionHandler, String currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(sessionHandler.getLoginID()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        return params;
    }

}
