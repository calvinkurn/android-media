package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupNewPromoFragment;

import static com.tokopedia.seller.topads.view.fragment.TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AD_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO top ads new groups/edit existing group/promo not in group has been success
            }
        }
    }
}