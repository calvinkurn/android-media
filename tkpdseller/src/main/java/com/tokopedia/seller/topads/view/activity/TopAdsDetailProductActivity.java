package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.ProductAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;

public class TopAdsDetailProductActivity extends TActivity implements TopAdsDetailProductFragment.TopAdsDetailProductFragmentListener {
    ProductAd productAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_product);

        if(getIntent() != null && getIntent().getExtras() != null) {
            productAd = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
        }

            getFragmentManager().beginTransaction().disallowAddToBackStack()
                    .replace(R.id.container, TopAdsDetailProductFragment.createInstance(productAd), TopAdsDetailProductFragment.class.getSimpleName())
                    .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void goToProductActivity(String productUrl) {
        if(getApplication() instanceof SellerModuleRouter){
            ((SellerModuleRouter)getApplication()).goToProductDetail(this, productUrl);
        }
    }
}
