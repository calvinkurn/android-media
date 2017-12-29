package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class GetHomeFeedsUseCase extends UseCase<FeedResult> {

    private final HomeFeedRepository feedRepository;

    public GetHomeFeedsUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               HomeFeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getHomeFeeds(requestParams);
    }

    public RequestParams getFeedPlusParam(int page, SessionHandler sessionHandler, String
            currentCursor) {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(sessionHandler.getLoginID()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        params.putInt(GetFeedsUseCase.PARAM_PAGE, page);
        return params;
    }
}
