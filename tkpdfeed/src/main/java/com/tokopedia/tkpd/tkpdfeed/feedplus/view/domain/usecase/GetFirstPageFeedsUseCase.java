package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class GetFirstPageFeedsUseCase extends UseCase<FeedResult> {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private FeedRepository feedRepository;

    public GetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread);

        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;

    }
    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return Observable.concat(feedRepository.getFirstPageFeedsFromLocal(), feedRepository.getFirstPageFeedsFromCloud())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends FeedResult>>() {
                    @Override
                    public Observable<? extends FeedResult> call(Throwable throwable) {
                        return feedRepository.getFirstPageFeedsFromCloud();
                    }
                });
    }
}
