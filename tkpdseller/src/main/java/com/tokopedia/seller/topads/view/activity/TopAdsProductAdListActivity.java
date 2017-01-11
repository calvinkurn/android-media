package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductAdListFragment;

public class TopAdsProductAdListActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int group = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_GROUP, 0);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsProductAdListFragment.createInstance(group), TopAdsProductAdListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
