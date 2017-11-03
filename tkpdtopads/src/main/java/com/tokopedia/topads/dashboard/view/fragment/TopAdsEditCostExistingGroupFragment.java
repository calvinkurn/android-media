package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.request.GetSuggestionBody;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostExistingGroupFragment extends TopAdsEditCostFragment<TopAdsDetailEditGroupPresenter, TopAdsDetailGroupViewModel, GroupAd> {

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
                daggerPresenter.saveAd(detailAd);
                trackingEditCostTopads();
            } else {
                hideLoading();
            }
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setSuggestionBidText(adFromIntent.getDatum().getMedianFmt());
    }

    private void trackingEditCostTopads() {
        if(detailAd.isBudget()) {
            UnifyTracking.eventTopAdsProductEditGrupCost(AppEventTracking.EventLabel.BUDGET_PER_DAY);
        }else{
            UnifyTracking.eventTopAdsProductEditGrupCost(AppEventTracking.EventLabel.BUDGET_NOT_LIMITED);
        }
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }

    @Override
    protected void loadSuggestionBid() {
        /* this is empty just to deal with abstraction */
    }

    public static Fragment createInstance(String adId, GroupAd groupAd){
        Fragment fragment = new TopAdsEditCostExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, groupAd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        setSuggestionBidText(s);
    }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {
        /* this is empty just to deal with abstraction */
    }

    @Override
    protected void onSuggestionTitleUseClick() {
        /* this is empty just to deal with abstraction */
    }
}
