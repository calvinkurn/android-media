package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordNewChooseGroupFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailEditShopFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsDetailNewGroupFragment;

/**
 * Created by nathan on 5/17/17.
 */

public class TopAdsKeywordNewChooseGroupActivity extends BaseActivity implements HasComponent<AppComponent> {

    public static final String TAG_FRAGMENT = TopAdsDetailEditShopFragment.class.getSimpleName();

    public static Intent createInstance(Context context){
        Intent intent = new Intent(context, TopAdsKeywordNewChooseGroupActivity.class);
        return intent;
    }

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
                .replace(R.id.container, getFragment(), TAG_FRAGMENT)
                .commit();
    }

    protected Fragment getFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if(fragment == null){
            fragment = TopAdsKeywordNewChooseGroupFragment.createInstance();
        }
        return fragment;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
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
