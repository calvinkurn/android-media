package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.view.fragment.TopAdsEditPromoProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsManageGroupPromoProductFragment;

/**
 * Created by zulfikarrahman on 2/27/17.
 */

public class TopAdsManageGroupPromoProductActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsManageGroupPromoProductFragment.createInstance(),
                        TopAdsManageGroupPromoProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
