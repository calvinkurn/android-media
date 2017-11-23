package com.tokopedia.topads.keyword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.topads.keyword.data.source.cloud.TopAdsKeywordGetDetailCloud;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordGetDetailDataSource {
    private final TopAdsKeywordGetDetailCloud topAdsKeywordGetDetailCloud;

    @Inject
    public TopAdsKeywordGetDetailDataSource(TopAdsKeywordGetDetailCloud topAdsKeywordGetDetailCloud) {
        this.topAdsKeywordGetDetailCloud = topAdsKeywordGetDetailCloud;
    }

    public Observable<PageDataResponse<List<Datum>>> getKeywordDetail(RequestParams requestParams) {
        return topAdsKeywordGetDetailCloud.getKeywordDetail(requestParams);
    }
}
