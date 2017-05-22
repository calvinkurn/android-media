package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.ProductFeedDomain;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class FeedRepositoryImpl  implements  FeedRepository{

    private FeedFactory feedFactory;

    private FeedRepositoryImpl(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public Observable<List<DataFeedDomain>> getFeeds() {
        return feedFactory.createCloudFeedDataSource().getFeedsList();
    }
}
