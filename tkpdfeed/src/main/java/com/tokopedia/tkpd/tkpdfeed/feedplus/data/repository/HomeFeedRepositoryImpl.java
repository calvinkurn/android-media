package com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.HomeFeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolCommentSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedRepositoryImpl implements HomeFeedRepository {

    private final HomeFeedFactory feedFactory;

    public HomeFeedRepositoryImpl(HomeFeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public Observable<FeedResult> getHomeFeeds(RequestParams requestParams) {
        return feedFactory.createHomeFeedDataSource().getHomeFeeds(requestParams);
    }
}
