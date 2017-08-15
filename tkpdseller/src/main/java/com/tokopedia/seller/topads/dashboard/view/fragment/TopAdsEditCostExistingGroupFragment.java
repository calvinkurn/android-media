package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailGroupViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditGroupPresenter;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditPresenter;

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
        super.onClickedNext();
        if(detailAd != null) {
            daggerPresenter.saveAd(detailAd);
        }else{
            hideLoading();
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
