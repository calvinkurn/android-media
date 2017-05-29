package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;

import java.util.List;

import rx.Observable;

/**
 * @author by nisie on 5/24/17.
 */

public class GetFeedsDetailUseCase extends UseCase<List<DataFeedDetailDomain>> {

    public static final String PARAM_DETAIL_ID = "PARAM_DETAIL_ID";
    public static final String PARAM_PAGE = "PARAM_PAGE";

    private FeedRepository feedRepository;

    public GetFeedsDetailUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }

    @Override
    public Observable<List<DataFeedDetailDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getFeedsDetail(requestParams);
    }
}
