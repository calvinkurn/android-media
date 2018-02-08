package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

public abstract class BaseSimpleActivity extends BaseToolbarActivity {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    protected abstract Fragment getNewFragment();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_seller;
    }

    protected void setupFragment(Bundle savedInstance) {
        if (savedInstance == null) {
            inflateFragment();
        }
    }

    protected void inflateFragment() {
        if (getNewFragment() == null)
            return;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, getNewFragment(), getTagFragment())
                .commit();
    }

    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag(getTagFragment());
    }

    protected String getTagFragment() {
        return TAG_FRAGMENT;
    }


}