package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditScheduleExistingGroupFragment extends TopAdsNewScheduleFragment<StepperModel, TopAdsDetailGroupViewModel, TopAdsDetailEditGroupPresenter> {

    public static Fragment createInstance(String adId) {
        Fragment fragment = new TopAdsEditScheduleExistingGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, adId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        headerText.setVisibility(View.GONE);
        submitButton.setText(getString(R.string.label_top_ads_save));
    }

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
    protected void onNextClicked() {
        super.onNextClicked();
        if(detailAd != null) {
            daggerPresenter.saveAd(detailAd);
        }
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        getActivity().finish();
    }

    @Override
    protected TopAdsDetailGroupViewModel initiateDetailAd() {
        return new TopAdsDetailGroupViewModel();
    }
}
