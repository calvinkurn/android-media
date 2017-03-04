package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewProductFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailNewProductActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailNewProductFragment.createInstance(), TopAdsDetailEditProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}