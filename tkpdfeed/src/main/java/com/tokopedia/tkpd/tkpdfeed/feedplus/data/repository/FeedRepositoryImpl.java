package com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.FeedResult;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class FeedRepositoryImpl implements FeedRepository{

    private FeedFactory feedFactory;

    public FeedRepositoryImpl(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFeedDataSource().getNextPageFeedsList(requestParams);

    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams requestParams) {
        return feedFactory.createCloudFeedDataSource().getFirstPageFeedsList(requestParams);
    }

    @Override
    public Observable<FeedResult> getFirstPageFeedsFromLocal() {
        return feedFactory.createLocalFeedDataSource().getFeeds();
    }

    @Override
    public Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams) {
        return feedFactory.createCloudDetailFeedDataSource().getFeedsDetailList(requestParams);
    }
}
