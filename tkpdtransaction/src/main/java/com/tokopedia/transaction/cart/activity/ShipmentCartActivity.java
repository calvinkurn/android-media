package com.tokopedia.transaction.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.cart.fragment.ShipmentCartFragment;
import com.tokopedia.transaction.cart.model.ShipmentCartPassData;


/**
 * @author anggaprasetiyo on 11/2/16.
 */

public class ShipmentCartActivity extends BasePresenterActivity {

    private static final String EXTRA_SHIPMENT_CART_PASS_DATA = "EXTRA_SHIPMENT_CART_PASS_DATA";
    private ShipmentCartPassData shipmentCartPassData;

    public static Intent createInstance(Context context, ShipmentCartPassData passData) {
        Intent intent = new Intent(context, ShipmentCartActivity.class);
        intent.putExtra(EXTRA_SHIPMENT_CART_PASS_DATA, passData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        shipmentCartPassData = extras.getParcelable(EXTRA_SHIPMENT_CART_PASS_DATA);
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
                ShipmentCartFragment.newInstance(shipmentCartPassData)).commit();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
