package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.ShopAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsEditPromoShopFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsNewPromoFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsEditPromoShopActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String shopAdId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            shopAdId = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsEditPromoShopFragment.createInstance(shopAdId), TopAdsNewPromoFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}