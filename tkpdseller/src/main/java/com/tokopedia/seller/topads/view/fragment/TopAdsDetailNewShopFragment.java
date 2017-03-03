package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewShopDI;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewShopPresenter;

public class TopAdsDetailNewShopFragment extends TopAdsDetailNewFragment<TopAdsDetailNewShopPresenter> {

    public static Fragment createInstance(String shopName) {
        Fragment fragment = new TopAdsDetailNewShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_NAME, shopName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_detail_edit_shop;
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        nameEditText.setEnabled(false);
        promoIconView.setVisibility(View.GONE);
        detailAd = new TopAdsDetailShopViewModel();
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        presenter.saveAd((TopAdsDetailShopViewModel) detailAd);
    }

    @Override
    public void onSaveAdSuccess(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.onSaveAdSuccess(topAdsDetailAdViewModel);
        goToShopItem(String.valueOf(topAdsDetailAdViewModel.getId()));
    }

    private void goToShopItem(String shopId) {
        Intent intent = new Intent(getActivity(), TopAdsDetailShopActivity.class);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, shopId);
        startActivity(intent);
    }

    @Override
    protected void addProduct() {

    }
}