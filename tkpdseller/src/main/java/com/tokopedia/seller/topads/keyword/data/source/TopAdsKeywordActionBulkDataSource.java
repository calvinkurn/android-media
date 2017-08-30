package com.tokopedia.seller.topads.keyword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;
import com.tokopedia.seller.topads.keyword.data.source.cloud.TopAdsKeywordActionBulkDataSourceCloud;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/29/17.
 */

public class TopAdsKeywordActionBulkDataSource {
    private final TopAdsKeywordActionBulkDataSourceCloud topAdsKeywordActionBulkDataSourceCloud;

    @Inject
    public TopAdsKeywordActionBulkDataSource(TopAdsKeywordActionBulkDataSourceCloud topAdsKeywordActionBulkDataSourceCloud) {
        this.topAdsKeywordActionBulkDataSourceCloud = topAdsKeywordActionBulkDataSourceCloud;
    }

    public Observable<PageDataResponse<DataBulkKeyword>> actionBulk(RequestParams requestParams) {
        return topAdsKeywordActionBulkDataSourceCloud.actionBulk(requestParams);
    }
}
