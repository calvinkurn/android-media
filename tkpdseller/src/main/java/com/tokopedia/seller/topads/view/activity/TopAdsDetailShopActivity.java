package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.ShopAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailShopFragment;

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

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailShopFragment.createInstance(ad, adId);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}