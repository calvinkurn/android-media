package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditShopFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailEditShopActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String shopName = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            shopName = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_SHOP_NAME);
            adId = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsDetailEditShopFragment.createInstance(shopName, adId), TopAdsDetailEditShopFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}