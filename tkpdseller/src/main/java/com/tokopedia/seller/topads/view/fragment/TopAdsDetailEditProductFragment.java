package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsEditPromoProductDI;
import com.tokopedia.seller.topads.view.activity.TopAdsAddProductListActivity;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailEditProductPresenter;

public class TopAdsDetailEditProductFragment extends TopAdsDetailEditFragment<TopAdsDetailEditProductPresenter> {

    public static Fragment createInstance(String shopAdId) {
        Fragment fragment = new TopAdsDetailEditProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsEditPromoProductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        promoIconView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }
}