package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public class GetRecentViewUseCase extends UseCase<List<RecentViewProductDomain>> {
    public static final String PARAM_USER_ID = "user_id";

    private final FeedRepository feedRepository;

    public GetRecentViewUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FeedRepository feedRepository) {
        super(threadExecutor, postExecutionThread);
        this.feedRepository = feedRepository;
    }


    @Override
    public Observable<List<RecentViewProductDomain>> createObservable(RequestParams requestParams) {
        return feedRepository.getRecentViewProduct(requestParams);
    }

    public RequestParams getParam(String loginID) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, loginID);
        return params;
    }
}
