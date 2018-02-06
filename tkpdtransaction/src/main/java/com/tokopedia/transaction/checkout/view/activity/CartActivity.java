package com.tokopedia.transaction.checkout.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.CartFragment;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartActivity extends BasePresenterActivity {
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
        return R.layout.activity_cart_tx_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    CartFragment.newInstance()).commit();
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
