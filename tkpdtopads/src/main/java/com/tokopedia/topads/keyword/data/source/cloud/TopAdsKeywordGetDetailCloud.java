package com.tokopedia.seller.topads.keyword.data.source.cloud;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.Datum;
import com.tokopedia.seller.topads.keyword.data.source.cloud.api.KeywordApi;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordGetDetailCloud {

    private final KeywordApi keywordApi;

    @Inject
    public TopAdsKeywordGetDetailCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }

    public Observable<PageDataResponse<List<Datum>>> getKeywordDetail(RequestParams requestParams) {
        return keywordApi.getDashboardKeyword(requestParams.getParamsAllValueInString());
    }
}
