package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_add_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsAddCreditFragment.createInstance(), TopAdsAddCreditFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}