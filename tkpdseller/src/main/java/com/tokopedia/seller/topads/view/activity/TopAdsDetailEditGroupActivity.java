package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditGroupFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailEditGroupActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        String name = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            name = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_NAME);
            adId = getIntent().getExtras().getString(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailEditGroupFragment.createInstance(name, adId), TopAdsDetailNewGroupFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}