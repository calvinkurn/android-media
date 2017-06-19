package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class TopAdsBaseSimpleActivity extends BaseActivity implements HasComponent<AppComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(com.tokopedia.core.R.style.Theme_Tokopedia3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.green_600));
        }

        setContentView(R.layout.activity_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedinstancestate) {
        if (savedinstancestate == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent_view, getNewFragment(), getTagFragment())
                    .commit();
        }
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

    @NonNull
    protected abstract Fragment getNewFragment();

    @NonNull
    protected abstract String getTagFragment();

}