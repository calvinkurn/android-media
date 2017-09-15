package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostExistingGroupFragment extends TopAdsEditCostFragment<TopAdsDetailEditGroupPresenter, TopAdsDetailGroupViewModel> {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .appComponent(getComponent(AppComponent.class))
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

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditCostExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
