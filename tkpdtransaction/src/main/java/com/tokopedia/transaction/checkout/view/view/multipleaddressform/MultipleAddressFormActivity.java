package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ResetShipmentFormDialog;

/**
 * Created by kris on 2/22/18. Tokopedia
 */

public class MultipleAddressFormActivity extends BasePresenterActivity {
    public static final int REQUEST_CODE = 982;

    private static final String EXTRA_CART_LIST_DATA = "EXTRA_CART_LIST_DATA";
    private static final String EXTRA_RECIPIENT_ADDRESS_DATA = "EXTRA_RECIPIENT_ADDRESS_DATA";
    public static final int RESULT_CODE_SUCCESS_SET_SHIPPING = 22;
    public static final int RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM = 23;

    private CartListData cartListData;
    private RecipientAddressModel addressData;

    public static Intent createInstance(Context context,
                                        CartListData cartListData,
                                        RecipientAddressModel recipientAddressData) {
        Intent intent = new Intent(context, MultipleAddressFormActivity.class);
        intent.putExtra(EXTRA_CART_LIST_DATA, cartListData);
        intent.putExtra(EXTRA_RECIPIENT_ADDRESS_DATA, recipientAddressData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.cartListData = extras.getParcelable(EXTRA_CART_LIST_DATA);
        this.addressData = extras.getParcelable(EXTRA_RECIPIENT_ADDRESS_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.multiple_address_form_activity;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof MultipleAddressFragment)) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, MultipleAddressFragment.newInstance(
                            cartListData,
                            addressData))
                    .commit();
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
    public void onBackPressed() {
        DialogFragment dialog = ResetShipmentFormDialog.newInstance(
                new ResetShipmentFormDialog.ResetShipmentFormCallbackAction() {

                    @Override
                    public void onResetCartShipmentForm() {
                        setResult(RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM);
                        finish();
                    }

                    @Override
                    public void onCancelResetCartShipmentForm() {

                    }
                });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(dialog, ResetShipmentFormDialog.DIALOG_FRAGMENT_TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
