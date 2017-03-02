package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddCreditFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsPaymentCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsPaymentCreditActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsPaymentCreditFragment.createInstance(), TopAdsPaymentCreditFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}