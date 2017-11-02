package com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feeddetail.DataFeedDetailDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.recentview.RecentViewProductDomain;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolComments;

import java.util.List;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface FeedRepository {

    Observable<FeedResult> getFeedsFromCloud(RequestParams requestParams);

    Observable<FeedResult> getFirstPageFeedsFromCloud(RequestParams parameters);

    Observable<FeedResult> getFirstPageFeedsFromLocal();

    Observable<List<DataFeedDetailDomain>> getFeedsDetail(RequestParams requestParams);

    Observable<List<RecentViewProductDomain>> getRecentViewProduct(RequestParams requestParams);

    Observable<CheckFeedDomain> checkNewFeed(RequestParams parameters);


    Observable<KolComments> getKolComments(RequestParams requestParams);
}
