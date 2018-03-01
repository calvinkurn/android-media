package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFragment;

/**
 * @author anggaprasetiyo on 25/01/18.
 */

public class CartShipmentActivity extends BasePresenterActivity implements ICartShipmentActivity {
    public static final int REQUEST_CODE = CartShipmentActivity.class.hashCode();
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 1;

    public static final String EXTRA_SHIPMENT_FORM_DATA = "EXTRA_SHIPMENT_FORM_DATA";
    public static final String EXTRA_SELECTED_ADDRESS_RECIPIENT_DATA = "EXTRA_DEFAULT_ADDRESS_RECIPIENT_DATA";
    public static final String EXTRA_CART_PROMO_SUGGESTION = "EXTRA_CART_PROMO_SUGGESTION";
    public static final String EXTRA_ADDRESS_SHIPMENT_TYPE = "EXTRA_ADDRESS_SHIPMENT_TYPE";
    public static final int TYPE_ADDRESS_SHIPMENT_SINGLE = 1;
    public static final int TYPE_ADDRESS_SHIPMENT_MULTIPLE = 2;

    private int typeAddressShipment;
    private CartShipmentAddressFormData cartShipmentAddressFormData;
    private CartPromoSuggestion cartPromoSuggestionData;

    public static Intent createInstanceSingleAddress(Context context,
                                                     CartShipmentAddressFormData cartShipmentAddressFormData,
                                                     CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_SINGLE);
        return intent;
    }

    public static Intent createInstanceMultipleAddress(Context context,
                                                       CartShipmentAddressFormData cartShipmentAddressFormData,
                                                       CartPromoSuggestion cartPromoSuggestion) {
        Intent intent = new Intent(context, CartShipmentActivity.class);
        intent.putExtra(EXTRA_SHIPMENT_FORM_DATA, cartShipmentAddressFormData);
        intent.putExtra(EXTRA_CART_PROMO_SUGGESTION, cartPromoSuggestion);
        intent.putExtra(EXTRA_ADDRESS_SHIPMENT_TYPE, TYPE_ADDRESS_SHIPMENT_MULTIPLE);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        typeAddressShipment = extras.getInt(EXTRA_ADDRESS_SHIPMENT_TYPE);
        cartShipmentAddressFormData = extras.getParcelable(EXTRA_SHIPMENT_FORM_DATA);
        cartPromoSuggestionData = extras.getParcelable(EXTRA_CART_PROMO_SUGGESTION);
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
                || (fragment instanceof SingleAddressShipmentFragment))) {
            if (typeAddressShipment == TYPE_ADDRESS_SHIPMENT_SINGLE) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        SingleAddressShipmentFragment.newInstance(
                                cartShipmentAddressFormData, cartPromoSuggestionData
                        )).commit();
            } else {
                //TODO Change Later
                /*getFragmentManager().beginTransaction().replace(R.id.container,
                        MultipleAddressShipmentFragment.newInstance(
                                cartShipmentAddressFormData, cartPromoSuggestionData
                        )).commit();*/
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void closeWithResult(int resultCode, @Nullable Intent intent) {
        if (intent == null) setResult(resultCode);
        else setResult(resultCode, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
