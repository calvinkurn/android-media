package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupAdListFragment;

public class TopAdsDetailProductActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_detail_product);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsGroupAdListFragment.createInstance(), TopAdsGroupAdListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return "TopAdsDetailProductActivity";
    }
}
