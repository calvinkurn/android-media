package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewShopFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailNewGroupActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String groupName = null;
        String groupId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            groupName = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_GROUP_NAME);
            groupId = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_GROUP_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsDetailNewGroupFragment.createInstance(groupName, groupId), TopAdsDetailNewShopFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}