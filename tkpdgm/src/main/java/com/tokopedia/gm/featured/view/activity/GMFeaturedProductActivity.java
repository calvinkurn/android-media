package com.tokopedia.gm.featured.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.gm.GMModuleRouter;
import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.featured.view.fragment.GMFeaturedProductFragment;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductActivity extends BaseSimpleActivity implements HasComponent<GMComponent>{

    private static final String TAG = "GMFeaturedProductActivity";

    @Override
    protected Fragment getNewFragment() {
        return GMFeaturedProductFragment.createInstance();
    }

    @Override
    protected String getTagFragment() {
        return TAG;
    }

    @Override
    public GMComponent getComponent() {
        if(getApplication() != null && getApplication() instanceof GMModuleRouter){
            return ((GMModuleRouter) getApplication()).getGMComponent();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragment();
        if(fragment != null && fragment.isVisible() && fragment instanceof GMFeaturedProductFragment){
            ((GMFeaturedProductFragment) fragment).onBackPressed();
        }
    }
}
