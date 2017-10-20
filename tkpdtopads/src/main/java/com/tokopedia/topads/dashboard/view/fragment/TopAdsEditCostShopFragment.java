package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostShopFragment extends TopAdsEditCostFragment<TopAdsDetailEditShopPresenter, TopAdsDetailShopViewModel> {

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerTopAdsCreatePromoComponent.builder()
                .topAdsCreatePromoModule(new TopAdsCreatePromoModule())
                .topAdsComponent(getTopAdsComponent())
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
            }
        }
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }

    public static Fragment createInstance(String shopId) {
        Fragment fragment = new TopAdsEditCostShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
