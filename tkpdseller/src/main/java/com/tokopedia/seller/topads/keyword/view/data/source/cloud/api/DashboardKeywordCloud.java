package com.tokopedia.seller.topads.keyword.view.data.source.cloud.api;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.view.data.model.KeywordDashBoard;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class DashboardKeywordCloud {

    private KeywordApi keywordApi;

    @Inject
    public DashboardKeywordCloud(KeywordApi keywordApi) {
        this.keywordApi = keywordApi;
    }

    public Observable<Response<KeywordDashBoard>> getDashboardKeyword(RequestParams requestParams) {
        return keywordApi.getDashboardKeyword(requestParams.getParamsAllValueInString());
    }
}
