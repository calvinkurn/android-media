package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class TopAdsBaseSimpleActivity extends TActivity implements HasComponent<AppComponent> {

    public static final String TAG_FRAGMENT = TopAdsBaseSimpleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedinstancestate) {
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .replace(R.id.parent_view, getFragment(savedinstancestate), TAG_FRAGMENT)
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