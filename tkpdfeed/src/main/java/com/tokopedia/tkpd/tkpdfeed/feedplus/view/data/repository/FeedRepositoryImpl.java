package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;

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
    public Observable<List<DataFeedDomain>> getFeeds(RequestParams params) {
        return feedFactory.createCloudFeedDataSource().getFeedsList(params);
    }

    @Override
    public Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams) {
        return feedFactory.createCloudDetailFeedDataSource().getFeedsDetailList(requestParams);
    }
}
