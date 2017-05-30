package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;

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

    public RequestParams getFeedDetailParam(String detailId, int page) {
        RequestParams params = RequestParams.create();
        params.putString(GetFeedsDetailUseCase.PARAM_DETAIL_ID, detailId);
        params.putInt(GetFeedsDetailUseCase.PARAM_PAGE, page);
        return params;
    }
}
