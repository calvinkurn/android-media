package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsSingleListFragment;

public class TopAdsSingleListActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsSingleListFragment.createInstance(), TopAdsSingleListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return "TopAdsSingleListActivity";
    }
}
