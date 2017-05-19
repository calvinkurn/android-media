package com.tokopedia.seller.topads.keyword.view.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.view.data.mapper.KeywordDashboardMapper;
import com.tokopedia.seller.topads.keyword.view.data.source.cloud.api.DashboardKeywordCloud;
import com.tokopedia.seller.topads.keyword.view.domain.model.KeywordDashboardDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardDataSouce {

    private DashboardKeywordCloud dashboardKeywordCloud;
    private KeywordDashboardMapper keywordDashboardMapper;

    @Inject
    public KeywordDashboardDataSouce(
            DashboardKeywordCloud dashboardKeywordCloud,
            KeywordDashboardMapper keywordDashboardMapper
    ) {
        this.dashboardKeywordCloud = dashboardKeywordCloud;
        this.keywordDashboardMapper = keywordDashboardMapper;
    }

    public Observable<KeywordDashboardDomain> getKeywordDashboard(RequestParams requestParams) {
        return dashboardKeywordCloud.getDashboardKeyword(requestParams).map(keywordDashboardMapper);
    }


}
