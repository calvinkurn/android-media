package com.tokopedia.transaction.checkout.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.CartFragment;
import com.tokopedia.transaction.checkout.view.CartRemoveProductFragment;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checkout_cart_remove, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cart_remove) {
            Fragment fragment = CartRemoveProductFragment.newInstance();

            FragmentManager fragmentManager = getFragmentManager();
            String backStateName = fragment.getClass().getName();

            boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            if (!isFragmentPopped) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(backStateName)
                        .commit();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart_tx_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof CartFragment)) {
            getFragmentManager().beginTransaction().replace(R.id.container,
                    CartFragment.newInstance()).commit();
        }
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
