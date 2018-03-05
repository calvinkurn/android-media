package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSellerItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 05/02/18.
 */

public class CartAddressChoiceActivity extends BasePresenterActivity implements ICartAddressChoiceActivityListener {
    public static final int REQUEST_CODE = CartAddressChoiceActivity.class.hashCode();
    public static final int RESULT_CODE_ACTION_SELECT_ADDRESS = 100;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 101;
    private static final String CART_ITEM_LIST_EXTRA = "CART_ITEM_LIST_EXTRA";
    private static final String EXTRA_TYPE_REQUEST = "EXTRA_TYPE_REQUEST";
    public static final String EXTRA_DEFAULT_SELECTED_ADDRESS = "EXTRA_DEFAULT_SELECTED_ADDRESS";
    public static final String EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA";
    public static final int TYPE_REQUEST_ONLY_ADDRESS_SELECTION = 0;
    public static final int TYPE_REQUEST_FULL_SELECTION = 1;


    private int typeRequest;
    private RecipientAddressModel defaultRecipientAddressModel;

    public static Intent createInstance(Activity activity, int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.typeRequest = extras.getInt(EXTRA_TYPE_REQUEST);
        this.defaultRecipientAddressModel = extras.getParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setupToolbar();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        Fragment fragment;

        if (typeRequest == TYPE_REQUEST_FULL_SELECTION) {
            // Go to short listed address
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS, defaultRecipientAddressModel);

            fragment = CartAddressChoiceFragment.newInstance();
            fragment.setArguments(bundle);

        } else {
            // Go to full address list
            fragment = ShipmentAddressListFragment.newInstance();
        }

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment, fragment.getClass().getSimpleName())
                .commit();

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
    public void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
        setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
        finish();
    }

    @Override
    public void finishSendResultActionToMultipleAddressForm() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, defaultRecipientAddressModel);
        setResult(RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM);
        finish();
    }
}
