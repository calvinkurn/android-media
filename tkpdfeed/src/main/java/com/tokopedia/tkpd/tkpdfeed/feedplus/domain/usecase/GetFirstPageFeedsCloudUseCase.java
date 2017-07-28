package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;

import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author by nisie on 7/25/17.
 */

public class GetFirstPageFeedsCloudUseCase extends GetFeedsUseCase {

    GetRecentViewUseCase getRecentProductUseCase;

    public GetFirstPageFeedsCloudUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         FeedRepository feedRepository,
                                         GetRecentViewUseCase getRecentProductUseCase) {
        super(threadExecutor, postExecutionThread, feedRepository);
        this.getRecentProductUseCase = getRecentProductUseCase;
    }

    @Override
    public Observable<FeedResult> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getFeedPlus(requestParams),
                getRecentView(requestParams),
                new Func2<FeedResult, List<RecentViewProductDomain>, FeedResult>() {
                    @Override
                    public FeedResult call(FeedResult feedResult, List<RecentViewProductDomain> recentViewProductDomains) {
                        feedResult.getFeedDomain().setRecentProduct(recentViewProductDomains);
                        return feedResult;
                    }
                }
        );
    }

    private Observable<FeedResult> getFeedPlus(RequestParams requestParams) {
        return feedRepository.getFirstPageFeedsFromCloud(requestParams);
    }

    private Observable<List<RecentViewProductDomain>> getRecentView(RequestParams requestParams) {
        return getRecentProductUseCase.createObservable(requestParams);
    }
}
