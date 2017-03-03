package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.ShopAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailShopFragment;

public class TopAdsDetailShopActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_shop);
        ShopAd ad = null;
        int adId = -1;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_AD_ID, -1);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailShopFragment.createInstance(ad, adId), TopAdsDetailProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}