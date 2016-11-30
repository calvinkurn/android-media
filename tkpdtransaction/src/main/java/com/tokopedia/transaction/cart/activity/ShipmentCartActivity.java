package com.tokopedia.transaction.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.cart.fragment.ShipmentCartFragment;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;


/**
 * @author anggaprasetiyo on 11/2/16.
 */

public class ShipmentCartActivity extends BasePresenterActivity {
    public static final int INTENT_REQUEST_CODE = BasePresenterActivity.class.hashCode();
    private static final String EXTRA_CART_DATA = "EXTRA_CART__DATA";
    private TransactionList cartData;

    public static Intent createInstance(Context context, TransactionList cartData) {
        Intent intent = new Intent(context, ShipmentCartActivity.class);
        intent.putExtra(EXTRA_CART_DATA, cartData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.cartData = extras.getParcelable(EXTRA_CART_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipment_cart;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewListener() {
        getFragmentManager().beginTransaction().add(R.id.container,
                ShipmentCartFragment.newInstance(cartData)).commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
