package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailGroupFragment;

public class TopAdsDetailGroupActivity extends TActivity {

    public static final String TAG = TopAdsDetailGroupFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_group);

        GroupAd ad = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        }

        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailGroupFragment.createInstance(ad, adId);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment, TAG)
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
