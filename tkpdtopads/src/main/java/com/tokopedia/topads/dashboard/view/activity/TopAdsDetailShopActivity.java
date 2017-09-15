package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.ShopAd;
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDetailShopFragment;

public class TopAdsDetailShopActivity extends TActivity {

    public static final String TAG = TopAdsDetailShopFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_shop);
        ShopAd ad = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailShopFragment.createInstance(ad, adId);
        }
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}