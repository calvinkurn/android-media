package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditShopFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;

/**
 * Created by nathan on 5/17/17.
 */

public class TopAdsKeywordNewChooseGroupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_edit_promo);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupFragment();
    }

    private void setupFragment() {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, getFragment(), TopAdsDetailEditShopFragment.class.getSimpleName())
                .commit();
    }

    protected Fragment getFragment() {
        return TopAdsKeywordNewChooseGroupFragment.createInstance();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }
}
