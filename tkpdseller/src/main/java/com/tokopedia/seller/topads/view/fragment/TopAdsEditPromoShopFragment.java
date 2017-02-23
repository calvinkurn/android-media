package com.tokopedia.seller.topads.view.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;

public class TopAdsEditPromoShopFragment extends TopAdsEditPromoFragment implements TopAdsEditPromoFragmentListener {

    public static Fragment createInstance(String shopAdId) {
        Fragment fragment = new TopAdsEditPromoShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TopAdsExtraConstant.EXTRA_AD_ID, shopAdId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_shop;
    }

}