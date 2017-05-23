package com.tokopedia.seller.topads.keyword.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDashboardDomain;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public interface TopAdsKeywordRepository {
    Observable<KeywordDashboardDomain> getDashboardKeyword(RequestParams requestParams);
}
