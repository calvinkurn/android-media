package com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.DataFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail.DataFeedDetailDomain;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface FeedRepository {

    Observable<List<DataFeedDomain>> getFeeds(RequestParams requestParams);

    Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams);


}
