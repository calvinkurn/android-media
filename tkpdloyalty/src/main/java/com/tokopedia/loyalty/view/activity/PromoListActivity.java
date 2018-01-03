package com.tokopedia.loyalty.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoListActivity extends BasePresenterActivity implements HasComponent<AppComponent> {


    @Override
    public AppComponent getComponent() {
        return null;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_promo_list;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof PromoListFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    PromoListFragment.newInstance()).commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
