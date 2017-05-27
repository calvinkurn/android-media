package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class GetFeedsUseCase extends UseCase<FeedResult> {
    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_CURSOR = "PARAM_CURSOR";
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
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return feedRepository.getFeedsFromCloud(requestParams);
    }
}
