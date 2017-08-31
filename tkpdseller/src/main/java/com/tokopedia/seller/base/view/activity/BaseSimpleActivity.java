package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class BaseSimpleActivity extends BaseToolbarActivity{

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    protected abstract Fragment getNewFragment();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_toolbar;
    }

    protected void setupFragment(Bundle savedinstancestate) {
        if (savedinstancestate == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent_view, getNewFragment(), getTagFragment())
                    .commit();
        }
    }

    protected String getTagFragment() {
        return TAG_FRAGMENT;
    }


}