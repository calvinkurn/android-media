package com.tokopedia.transaction.checkout.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.CartSingleAddressFragment;
import com.tokopedia.transaction.checkout.view.MultipleAddressFragment;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 25/01/18.
 */

public class CartShipmentActivity extends BasePresenterActivity implements ICartShipmentActivity {
    public static final String EXTRA_CART_DATA_LIST = "EXTRA_CART_DATA_LIST";
    public static final String EXTRA_ADDRESS_SHIPMENT_TYPE = "EXTRA_ADDRESS_SHIPMENT_TYPE";
    public static final int TYPE_ADDRESS_SHIPMENT_SINGLE = 1;
    public static final int TYPE_ADDRESS_SHIPMENT_MULTIPLE = 2;

    private int typeAddressShipment;
    private List<CartItemData> cartItemDataList;

    public static Intent createInstanceSingleAddress(Context context, List<CartItemData> cartItemDataList) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CART_DATA_LIST, (ArrayList<? extends Parcelable>) cartItemDataList);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_SINGLE);
        return intent;
    }

    public static Intent createInstanceMultipleAddress(Context context, List<CartItemData> cartItemDataList) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_CART_DATA_LIST, (ArrayList<? extends Parcelable>) cartItemDataList);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_MULTIPLE);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        typeAddressShipment = extras.getInt(EXTRA_ADDRESS_SHIPMENT_TYPE);
        cartItemDataList = extras.getParcelableArrayList(EXTRA_CART_DATA_LIST);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipment_cart_tx_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !((fragment instanceof MultipleAddressFragment)
                || (fragment instanceof CartSingleAddressFragment))) {
            if (typeAddressShipment == TYPE_ADDRESS_SHIPMENT_SINGLE) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        CartSingleAddressFragment.newInstance(cartItemDataList)).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        MultipleAddressFragment.newInstance()).commit();
            }
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

    @Override
    public List<CartItemData> getCartItemDataList() {
        return cartItemDataList;
    }

    @Override
    public void goToSingleAddressCart(Object data) {

    }

    @Override
    public void goToMultipleAddressCart(Object data) {

    }
}
