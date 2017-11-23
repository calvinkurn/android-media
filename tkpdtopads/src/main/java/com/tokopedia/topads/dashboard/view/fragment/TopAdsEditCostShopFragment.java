package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.di.component.DaggerTopAdsCreatePromoComponent;
import com.tokopedia.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.topads.dashboard.view.model.Ad;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;

/**
 * Created by zulfikarrahman on 8/8/17.
 */

public class TopAdsEditCostShopFragment extends TopAdsEditCostFragment<TopAdsDetailEditShopPresenter, TopAdsDetailShopViewModel, Ad> {

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
            }
        }
    }

    @Override
    protected TopAdsDetailShopViewModel initiateDetailAd() {
        return new TopAdsDetailShopViewModel();
    }

    @Override
    protected void loadSuggestionBid() {
        setSuggestionBidText((GetSuggestionResponse)null);
        titleSuggestionBidUse.setVisibility(View.GONE);
    }

    @Override
    protected void onSuggestionTitleUseClick() {
        titleSuggestionBid.setText(getString(R.string.static_suggestion_bid_recommendation));
    }

    public static Fragment createInstance(String shopId) {
        Fragment fragment = new TopAdsEditCostShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSuggestionSuccess(GetSuggestionResponse s) { /* remain empty*/ }

    @Override
    public void onSuggestionError(@Nullable Throwable t) {

    }
}
