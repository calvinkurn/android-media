package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupNewPromoFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsGroupNewPromoActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsGroupNewPromoFragment.createInstance(), TopAdsGroupNewPromoFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}