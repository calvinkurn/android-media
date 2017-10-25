package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostWithoutGroupFragment extends TopAdsEditCostFragment<TopAdsDetailEditProductPresenter, TopAdsDetailProductViewModel, GroupAd> {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void onClickedNext() {
        if(!isError()) {
            super.onClickedNext();
            if (detailAd != null) {
                trackingEditCostTopads();
                daggerPresenter.saveAd(detailAd);
            }
        }
    }

    private void trackingEditCostTopads() {
        if(detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductEditProductCost(AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsProductEditProductCost(AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailProductViewModel initiateDetailAd() {
        return new TopAdsDetailProductViewModel();
    }

    @Override
    protected void loadSuggestionBid() {

    }

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditCostWithoutGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onErrorLoadTopAdsProduct(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        setSuggestionBidText(s);
    }

    @Override
    protected void onSuggestionTitleUseClick() {

    }
}
