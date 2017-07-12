package com.tokopedia.seller.topads.keyword.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public interface TopAdsKeywordActionBulkRepository {
    Observable<PageDataResponse<DataBulkKeyword>> actionBulk(RequestParams requestParams);
}
