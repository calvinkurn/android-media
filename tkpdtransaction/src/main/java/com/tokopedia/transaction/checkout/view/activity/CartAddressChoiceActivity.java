package com.tokopedia.transaction.checkout.view.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.CartAddressChoiceFragment;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoiceActivity extends BasePresenterActivity {

    public static Intent createInstance(Activity activity) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        return intent;
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
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        CartAddressChoiceFragment fragment = CartAddressChoiceFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, CartAddressChoiceFragment.class.getSimpleName());
        fragmentTransaction.commit();
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
