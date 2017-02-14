package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddCreditFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsNewPromoFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsNewPromoActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsNewPromoFragment.createInstance(), TopAdsNewPromoFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}