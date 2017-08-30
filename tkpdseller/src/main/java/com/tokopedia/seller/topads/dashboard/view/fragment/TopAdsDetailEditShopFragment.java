package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.TopAdsDetailEditShopDI;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailEditShopPresenter;

public class TopAdsDetailEditShopFragment extends TopAdsDetailEditFragment<TopAdsDetailEditShopPresenter> {

    public static Fragment createInstance(String name, String shopAdId) {
        Fragment fragment = new TopAdsDetailEditShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, name);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_edit_shop;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailEditShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameEditText.setEnabled(false);
    }

    @Override
    protected void loadAdDetail() {
        super.loadAdDetail();
        presenter.getDetailAd(adId);
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (detailAd !=  null) {
            presenter.saveAd((TopAdsDetailShopViewModel) detailAd);
        }
    }

    @Override
    public void onSuccessLoadTopAdsProduct(TopAdsProductViewModel topAdsProductViewModel) {
        // no op
    }
}