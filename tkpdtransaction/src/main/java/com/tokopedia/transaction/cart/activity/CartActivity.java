package com.tokopedia.transaction.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.cart.fragment.CartFragment;


/**
 * @author anggaprasetiyo on 11/1/16.
 */

public class CartActivity extends BasePresenterActivity {

    public static Intent createInstance(Context context) {
        return new Intent(context, CartActivity.class);
    }


    @DeepLink(Constants.Applinks.CART)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Intent intent = new Intent(context, CartActivity.class).putExtras(extras);
        intent.putExtras(extras);
        if (extras.getString(DeepLink.URI) != null) {
            Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
            intent.setData(uri.build());
        }
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
        return R.layout.activity_cart_tx_module;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        getFragmentManager().beginTransaction().replace(R.id.container,
                CartFragment.newInstance()).commit();

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CART;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
