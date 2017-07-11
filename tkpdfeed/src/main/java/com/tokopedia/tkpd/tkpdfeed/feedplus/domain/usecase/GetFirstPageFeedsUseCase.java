package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class GetFirstPageFeedsUseCase extends GetFeedsUseCase {

    public GetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread, feedRepository);
    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        return Observable.concat(
                feedRepository.getFirstPageFeedsFromLocal(),
                feedRepository.getFirstPageFeedsFromCloud(requestParams))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends FeedResult>>() {
                    @Override
                    public Observable<? extends FeedResult> call(Throwable throwable) {
                        return feedRepository.getFirstPageFeedsFromCloud(requestParams);
                    }
                });
    }

}
