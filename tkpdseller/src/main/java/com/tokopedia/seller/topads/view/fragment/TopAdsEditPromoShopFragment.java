package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;

public class TopAdsEditPromoShopFragment extends TopAdsEditPromoFragment {

    private EditText shopNameEditText;
    private String shopName;

    public static Fragment createInstance(String shopName, String shopAdId) {
        Fragment fragment = new TopAdsEditPromoShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_SHOP_NAME, shopName);
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
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
    protected void initView(View view) {
        super.initView(view);
        shopNameEditText = (EditText) view.findViewById(R.id.edit_text_shop_name);
        shopNameEditText.setText(shopName);
    }

    @Override
    protected void loadAd(TopAdsDetailAdViewModel topAdsDetailAdViewModel) {
        super.loadAd(topAdsDetailAdViewModel);
        shopNameEditText.setText(topAdsDetailAdViewModel.getTitle());
    }
}