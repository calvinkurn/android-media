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
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordDetailNegativeFragment;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class TopAdsBaseActivity extends BaseActivity implements HasComponent<AppComponent> {


    public static final String TAG_FRAGMENT = TopAdsBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_base_layout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedinstancestate) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.container, getFragment(savedinstancestate), TAG_FRAGMENT)
                .commit();
    }

    protected Fragment getFragment(Bundle savedinstancestate) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if(fragment == null){
            fragment = getNewFragment(savedinstancestate);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract Fragment getNewFragment(Bundle savedinstancestate);

}
