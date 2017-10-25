package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailProductViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditProductPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleWithoutGroupFragment extends TopAdsNewScheduleFragment<StepperModel, TopAdsDetailProductViewModel, TopAdsDetailEditProductPresenter> {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(getComponent(TopAdsComponent.class))
                .build()
                .inject(this);
        daggerPresenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        headerText.setVisibility(View.GONE);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

    @Override
    protected void onNextClicked() {
        super.onNextClicked();
        if(detailAd != null) {
            trackingEditScheduleTopads();
            daggerPresenter.saveAd(detailAd);
        }
    }

    private void trackingEditScheduleTopads() {
        if(detailAd.isScheduled()) {
            UnifyTracking.eventTopAdsProductEditProductSchedule(AppEventTracking.EventLabel.SHOWTIME_SETUP);
        }else{
            UnifyTracking.eventTopAdsProductEditProductSchedule(AppEventTracking.EventLabel.SHOWTIME_AUTO);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        getActivity().finish();
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) {
        // TODO things need to be done
    }

    @Override
    protected TopAdsDetailProductViewModel initiateDetailAd() {
        return new TopAdsDetailProductViewModel();
    }

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditScheduleWithoutGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
