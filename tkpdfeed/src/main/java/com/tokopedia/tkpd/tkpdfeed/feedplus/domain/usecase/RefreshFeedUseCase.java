package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 7/5/17.
 */

public class RefreshFeedUseCase extends GetFeedsUseCase {

    public RefreshFeedUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread, feedRepository);
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);

    }
}
