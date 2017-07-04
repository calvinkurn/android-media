package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public class RefreshFeedsUseCase extends GetFirstPageFeedsUseCase {

    public RefreshFeedsUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread, feedRepository);
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);
    }
}
