package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.ProductAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;

public class TopAdsDetailProductActivity extends TActivity implements TopAdsDetailProductFragment.TopAdsDetailProductFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_product);

        ProductAd ad = null;
        int adId = -1;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_AD_ID, -1);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsDetailProductFragment.createInstance(ad, adId), TopAdsDetailProductFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void goToProductActivity(String productUrl) {
        if (getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getApplication()).goToProductDetail(this, productUrl);
        }
    }
}
