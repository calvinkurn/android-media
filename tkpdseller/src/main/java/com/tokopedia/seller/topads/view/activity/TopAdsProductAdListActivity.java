package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsProductAdListFragment;

public class TopAdsProductAdListActivity extends TActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupAd groupAd = getIntent().getParcelableExtra(TopAdsExtraConstant.EXTRA_GROUP);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsProductAdListFragment.createInstance(groupAd), TopAdsProductAdListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
