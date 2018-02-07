package com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;

import rx.Observable;

/**
 * Created by henrypriyono on 12/29/17.
 */

public interface HomeFeedRepository {
    Observable<FeedResult> getHomeFeeds(RequestParams requestParams);
}
