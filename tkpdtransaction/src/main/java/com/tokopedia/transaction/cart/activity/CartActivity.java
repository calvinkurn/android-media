package com.tokopedia.transaction.cart.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.cart.fragment.CartFragment;
import com.tokopedia.transaction.cart.listener.ICartActionFragment;


/**
 * @author anggaprasetiyo on 11/1/16.
 */

public class CartActivity extends BasePresenterActivity implements ICartActionFragment {

    public static Intent createInstance(Context context) {
        return new Intent(context, CartActivity.class);
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
        return R.layout.activity_cart_revamp;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        getFragmentManager().beginTransaction().add(R.id.container,
                CartFragment.newInstance()).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    @Override
    public void replaceFragmentWithBackStack(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.container,
                fragment).addToBackStack(null).commit();
    }

    @Override
    public void onTopPaySuccess(String paymentId, String message) {

    }

    @Override
    public void onTopPayFailed(String message) {

    }

    @Override
    public void onTopPayCanceled(String message) {

    }
}
