package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFeedsUseCase extends UseCase<List<DataFeedDomain>> {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private FeedRepository feedRepository;

    public GetFeedsUseCase(ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread,
                           FeedRepository feedRepository) {

        super(threadExecutor, postExecutionThread);

        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.feedRepository = feedRepository;

    }

    @Override
    public Observable<List<DataFeedDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getFeeds(requestParams);
    }
}
