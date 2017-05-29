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
    public static final String PARAM_IS_LOAD_FROM_CACHE = "PARAM_IS_LOAD_FROM_CACHE";
    private FeedRepository feedRepository;

    public GetFirstPageFeedsUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;

    }

    @Override
    public Observable<FeedResult> createObservable(final RequestParams requestParams) {
        if (requestParams.getBoolean(PARAM_IS_LOAD_FROM_CACHE, false)) {
            return Observable.concat(
                    feedRepository.getFirstPageFeedsFromLocal(),
                    feedRepository.getFirstPageFeedsFromCloud(requestParams))
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends FeedResult>>() {
                        @Override
                        public Observable<? extends FeedResult> call(Throwable throwable) {
                            return feedRepository.getFirstPageFeedsFromCloud(requestParams);
                        }
                    });
        } else {
            return feedRepository.getFirstPageFeedsFromCloud(requestParams);

        }
    }
}
