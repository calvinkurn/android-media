package com.tokopedia.seller.topads.keyword.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordDetailFragment;

/**
 * Created by zulfikarrahman on 5/26/17.
 */

public class TopAdsKeywordDetailActivity extends BaseActivity implements HasComponent<AppComponent> {

    public static final String TAG_FRAGMENT = TopAdsKeywordDetailFragment.class.getSimpleName();

    public static Intent createInstance(Context context){
        Intent intent = new Intent(context, TopAdsKeywordNewChooseGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ads_keyword_detail);
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
            fragment = TopAdsKeywordDetailFragment.createInstance(null, "");
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
