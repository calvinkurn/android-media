package com.tokopedia.seller.topads.view.fragment;

import android.os.Bundle;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.listener.TopAdsDetailEditView;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewPresenter;

public abstract class TopAdsDetailEditFragment<T extends TopAdsDetailNewPresenter> extends TopAdsDetailNewFragment<T> implements TopAdsDetailEditView {

    protected String adId;

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        adId = bundle.getString(TopAdsExtraConstant.EXTRA_AD_ID);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadAdDetail();
    }

    protected void loadAdDetail() {
        showLoading();
    }

    @Override
    public void onDetailAdLoaded(TopAdsDetailAdViewModel detailAd) {
        hideLoading();
        loadAd(detailAd);
    }

    @Override
    public void onLoadDetailAdError() {
        hideLoading();
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                loadAdDetail();
                showLoading();
            }
        });
    }
}