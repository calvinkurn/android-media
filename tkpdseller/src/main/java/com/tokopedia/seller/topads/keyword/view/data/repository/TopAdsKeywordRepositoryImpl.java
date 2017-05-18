package com.tokopedia.seller.topads.keyword.view.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.keyword.view.data.source.KeywordDashboardDataSouce;
import com.tokopedia.seller.topads.keyword.view.domain.TopAdsKeywordRepository;
import com.tokopedia.seller.topads.keyword.view.domain.model.KeywordDashboardDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class TopAdsKeywordRepositoryImpl implements TopAdsKeywordRepository {

    private KeywordDashboardDataSouce keywordDashboardDataSouce;

    @Inject
    public TopAdsKeywordRepositoryImpl(KeywordDashboardDataSouce keywordDashboardDataSouce) {
        this.keywordDashboardDataSouce = keywordDashboardDataSouce;
    }

    @Override
    public Observable<KeywordDashboardDomain> getDashboardKeyword(RequestParams requestParams) {
        return keywordDashboardDataSouce.getKeywordDashboard(requestParams);
    }
}
