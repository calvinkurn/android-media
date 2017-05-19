package com.tokopedia.seller.topads.keyword.view.activity;

import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsKeywordListFragment;

/**
 * Created by nathan on 5/15/17.
 */

public class TopAdsKeywordListActivity extends TActivity implements HasComponent<AppComponent> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_add_product);
        getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsKeywordListFragment.createInstance(), TopAdsKeywordListFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
