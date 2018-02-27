package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;

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
    public static final String EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA";
    public static final int TYPE_REQUEST_ONLY_ADDRESS_SELECTION = 0;
    public static final int TYPE_REQUEST_FULL_SELECTION = 1;


    private int typeRequest;

    public static Intent createInstance(Activity activity,
                                        int typeRequest,
                                        List<CartSellerItemModel> cartSellerItemModelList) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        intent.putParcelableArrayListExtra(CART_ITEM_LIST_EXTRA,
                (ArrayList<? extends Parcelable>) cartSellerItemModelList);
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
        if (typeRequest == TYPE_REQUEST_FULL_SELECTION) {
            CartAddressChoiceFragment fragment = CartAddressChoiceFragment
                    .newInstance();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.add(R.id.container, fragment, CartAddressChoiceFragment.class.getSimpleName());
            fragmentTransaction.commit();
        } else {

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
    public void finishSendResultActionSelectedAddress(Object selectedAddressResult) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, (Parcelable) selectedAddressResult);
        setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
        finish();
    }

    @Override
    public void finishSendResultActionToMultipleAddressForm() {
        setResult(RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM);
        finish();
    }
}
