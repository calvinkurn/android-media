package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.ProductAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupAdListFragment;

public class TopAdsDetailProductActivity extends TActivity {
    ProductAd productAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_product);

        if(getIntent() != null && getIntent().getExtras() != null) {
            productAd = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_DETAIL_DATA);
        }

        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsDetailProductFragment.createInstance(productAd), TopAdsDetailProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}
