package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.GroupAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailGroupFragment;

public class TopAdsDetailGroupActivity extends TActivity {

    GroupAd groupAd;
    private int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_group);

        if (getIntent() != null && getIntent().getExtras() != null) {
            groupAd = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            groupId = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_AD_ID_GROUP, -1);
        }

        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailGroupFragment.createInstance(groupAd, groupId), TopAdsDetailGroupFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
