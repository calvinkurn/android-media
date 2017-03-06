package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewProductFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailEditProductActivity extends TActivity {

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
                .replace(R.id.container, TopAdsDetailEditProductFragment.createInstance(name, adId), TopAdsDetailNewProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}