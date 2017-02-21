package com.tokopedia.seller.topads.view.fragment;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;

public class TopAdsEditPromoShopFragment extends TopAdsEditPromoFragment implements TopAdsEditPromoFragmentListener {

    public static TopAdsEditPromoShopFragment createInstance() {
        TopAdsEditPromoShopFragment fragment = new TopAdsEditPromoShopFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_promo;
    }

}