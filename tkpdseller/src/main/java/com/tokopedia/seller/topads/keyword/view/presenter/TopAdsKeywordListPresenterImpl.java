package com.tokopedia.seller.topads.keyword.view.presenter;

import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.keyword.view.domain.interactor.KeywordDashboardUseCase;
import com.tokopedia.seller.topads.view.presenter.TopAdsAdListPresenter;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/17/17.
 */

public class TopAdsKeywordListPresenterImpl implements TopAdsAdListPresenter<GroupAd> {

    private KeywordDashboardUseCase keywordDashboardUseCase;

    @Inject
    public TopAdsKeywordListPresenterImpl(KeywordDashboardUseCase keywordDashboardUseCase) {
        this.keywordDashboardUseCase = keywordDashboardUseCase;
    }

    @Override
    public void unSubscribe() {

    }
}
