package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class GetFirstPageFeedsUseCase extends GetFeedsUseCase {

    GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase;

    public GetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    FeedRepository feedRepository,
                                    GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase) {
        super(threadExecutor, postExecutionThread, feedRepository);
        this.getFirstPageFeedsCloudUseCase = getFirstPageFeedsCloudUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.concat(
                feedRepository.getFirstPageFeedsFromLocal(),
                getFirstPageFeedsCloudUseCase.createObservable(requestParams))
                .onErrorResumeNext(getFirstPageFeedsCloudUseCase.createObservable(requestParams));
    }
}
