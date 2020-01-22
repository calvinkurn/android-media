package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 5/30/17.
 */

@Deprecated
public abstract class BaseSimpleActivity extends BaseToolbarActivity {

    private static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    protected abstract Fragment getNewFragment();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_simple_seller;
    }

    @Override
    protected boolean isToolbarWhite() {
        return GlobalConfig.isCustomerApp() || super.isToolbarWhite();
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
                .replace(getParent_view(), getNewFragment(), getTagFragment())
                .commit();
    }

    protected int getParent_view() {
        return R.id.parent_view;
    }

    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag(getTagFragment());
    }

    protected String getTagFragment() {
        return TAG_FRAGMENT;
    }


}