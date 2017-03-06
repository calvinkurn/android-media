package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailGroupFragment;

public class TopAdsDetailGroupActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_group);

        GroupAd ad = null;
        int adId = -1;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_AD_ID, -1);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailGroupFragment.createInstance(ad, adId), TopAdsDetailGroupFragment.class.getSimpleName())
                .commit();
    }



    @Override
    public String getScreenName() {
        return null;
    }
}
