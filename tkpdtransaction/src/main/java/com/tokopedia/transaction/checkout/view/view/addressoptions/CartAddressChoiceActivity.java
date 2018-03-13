package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;

/**
 * @author Irfan Khoirul on 05/02/18
 *         Aghny A. Putra on 07/02/18
 */

public class CartAddressChoiceActivity extends BasePresenterActivity
        implements ICartAddressChoiceActivityListener {

    public static final int REQUEST_CODE = 981;

    public static final int RESULT_CODE_ACTION_SELECT_ADDRESS = 100;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 101;

    private static final String EXTRA_TYPE_REQUEST = "EXTRA_TYPE_REQUEST";
    public static final String EXTRA_DEFAULT_SELECTED_ADDRESS = "EXTRA_DEFAULT_SELECTED_ADDRESS";
    public static final String EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA";

    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST = 0;
    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST = 1;

    private int mTypeRequest;

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
        mTypeRequest = extras.getInt(EXTRA_TYPE_REQUEST);
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

        switch (mTypeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST:
                fragment = CartAddressChoiceFragment.newInstance();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                break;

            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                fragment = ShipmentAddressListFragment.newInstance();
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                break;

            default:
                break;
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
    public void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult) {
        switch (mTypeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST:
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS, selectedAddressResult);

                Fragment fragment = CartAddressChoiceFragment.newInstance();
                fragment.setArguments(bundle);

                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                break;

            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void finishSendResultActionToMultipleAddressForm() {
        setResult(RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM);
        finish();
    }

}