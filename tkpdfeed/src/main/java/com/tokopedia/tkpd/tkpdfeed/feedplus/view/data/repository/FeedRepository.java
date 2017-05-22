package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository;

import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface FeedRepository {

    Observable<List<DataFeedDomain>> getFeeds();

}
