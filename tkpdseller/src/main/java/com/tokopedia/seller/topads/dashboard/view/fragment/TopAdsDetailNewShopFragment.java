package com.tokopedia.seller.topads.dashboard.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.di.TopAdsDetailNewShopDI;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.dashboard.view.presenter.TopAdsDetailNewShopPresenter;

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
        detailAd = new TopAdsDetailShopViewModel();
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        ((TopAdsDetailShopViewModel) detailAd).setShopId(Long.parseLong(SessionHandler.getShopID(getActivity())));
    }

    @Override
    protected void saveAd() {
        super.saveAd();
        if (detailAd != null) {
            presenter.saveAd((TopAdsDetailShopViewModel) detailAd);
        }
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