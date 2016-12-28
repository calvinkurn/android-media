package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupListFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsPaymentCreditFragment;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public class TopAdsGroupListActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_group_ad_list);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsGroupListFragment.createInstance(), TopAdsGroupListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
