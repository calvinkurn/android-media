package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsEditPromoNewProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsEditPromoProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsNewPromoFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsEditPromoNewProductActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_edit_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsEditPromoNewProductFragment.createInstance(), TopAdsNewPromoFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}