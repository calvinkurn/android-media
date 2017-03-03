package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.di.TopAdsDetailNewShopDI;
import com.tokopedia.seller.topads.view.activity.TopAdsDetailShopActivity;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;
import com.tokopedia.seller.topads.view.model.TopAdsDetailShopViewModel;
import com.tokopedia.seller.topads.view.presenter.TopAdsDetailNewShopPresenter;

public class TopAdsDetailNewShopFragment extends TopAdsDetailNewFragment<TopAdsDetailNewShopPresenter> {

    private EditText shopNameEditText;
    private String shopName;

    public static Fragment createInstance(String shopName) {
        Fragment fragment = new TopAdsDetailNewShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_SHOP_NAME, shopName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        shopName = bundle.getString(TopAdsExtraConstant.EXTRA_SHOP_NAME);
    }

    @Override
    protected void initialPresenter() {
        presenter = TopAdsDetailNewShopDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        promoIconView.setVisibility(View.GONE);
        shopNameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        shopNameEditText.setText(shopName);
        detailAd = new TopAdsDetailShopViewModel();
    }

    @Override
    protected void loadAd(TopAdsDetailAdViewModel detailAd) {
        super.loadAd(detailAd);
        shopNameEditText.setText(detailAd.getTitle());
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