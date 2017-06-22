package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.view.fragment.TopAdsGroupNewPromoFragment;

import static com.tokopedia.seller.topads.view.fragment.TopAdsGroupNewPromoFragment.REQUEST_CODE_AD_STATUS;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsGroupNewPromoActivity extends TActivity {

    // from deeplink
    String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_new_promo);
        initFromIntent();
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, TopAdsGroupNewPromoFragment.createInstance(itemId), TopAdsGroupNewPromoFragment.class.getSimpleName())
                .commit();
    }

    private void initFromIntent (){
        Intent intent = getIntent();
        if (intent != null) {
            itemId = intent.getStringExtra(TopAdsExtraConstant.EXTRA_ITEM_ID);
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_AD_STATUS) {
            if (resultCode == Activity.RESULT_OK) {
                // top ads new groups/edit existing group/promo not in group has been success
                Intent intent = new Intent();
                boolean adStatusChanged = data.getBooleanExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, false);
                if (adStatusChanged) {
                    intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            //coming from deeplink
            Intent intent = new Intent(this, TopAdsDashboardActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            super.onBackPressed();
        }
    }
}