package com.tokopedia.seller.topads.dashboard.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsDetailEditProductFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsDetailNewProductFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsDetailNewProductActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String itemIdToAdd = null;
        Intent intent = getIntent();
        if (intent!= null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            itemIdToAdd = extras.getString(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
        inflateView(R.layout.activity_top_ads_edit_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailNewProductFragment.createInstance(itemIdToAdd), TopAdsDetailEditProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}