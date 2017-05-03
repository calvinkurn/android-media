package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.data.model.data.ProductAd;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailProductFragment;

public class TopAdsDetailProductActivity extends TActivity implements TopAdsDetailProductFragment.TopAdsDetailProductFragmentListener {

    public static final String TAG = TopAdsDetailProductFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_detail_product);

        ProductAd ad = null;
        String adId = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            ad = getIntent().getExtras().getParcelable(TopAdsExtraConstant.EXTRA_AD);
            adId = getIntent().getStringExtra(TopAdsExtraConstant.EXTRA_AD_ID);
        }
        Fragment fragment = getFragmentManager().findFragmentByTag(TAG);
        if(fragment == null){
            fragment = TopAdsDetailProductFragment.createInstance(ad, adId);
        }
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, fragment,
                        TAG)
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
