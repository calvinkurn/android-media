package com.tokopedia.seller.topads.keyword.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.data.mapper.KeywordDashboardMapper;
import com.tokopedia.seller.topads.keyword.data.mapper.TopAdsKeywordEditDetailDataMapper;
import com.tokopedia.seller.topads.keyword.data.model.TopAdsKeywordEditDetailInputDataModel;
import com.tokopedia.seller.topads.keyword.data.source.cloud.api.DashboardKeywordCloud;
import com.tokopedia.seller.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.seller.topads.keyword.domain.model.KeywordDashboardDomain;
import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordDashboardDataSouce {

    private DashboardKeywordCloud dashboardKeywordCloud;
    private KeywordDashboardMapper keywordDashboardMapper;
    private TopAdsKeywordEditDetailDataMapper topAdsKeywordEditDetailDataMapper;

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


    public Observable<EditTopAdsKeywordDetailDomainModel> editTopAdsKeywordDetail(TopAdsKeywordEditDetailInputDomainModel modelInput) {
        TopAdsKeywordEditDetailInputDataModel dataModel = TopAdsKeywordEditDetailDataMapper.mapDomainToData(modelInput);
        return dashboardKeywordCloud.editTopAdsKeywordDetail(dataModel).map(topAdsKeywordEditDetailDataMapper);
    }
}
