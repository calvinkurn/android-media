package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

/**
 * Created by kris on 2/22/18. Tokopedia
 */

public class MultipleAddressFormActivity extends BasePresenterActivity {

    public static final int REQUEST_CODE = MultipleAddressFormActivity.class.hashCode();
    private static final String EXTRA_CART_LIST_DATA = "EXTRA_CART_LIST_DATA";
    private static final String EXTRA_RECIPIENT_ADDRESS_DATA = "EXTRA_RECIPIENT_ADDRESS_DATA";
    public static final int RESULT_CODE_SUCCESS_SET_SHIPPING = 22;

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
        FragmentManager fragmentManager = getFragmentManager();
        CartListData cartListData = getIntent()
                .getExtras()
                .getParcelable(EXTRA_CART_LIST_DATA);
        RecipientAddressModel addressModel = getIntent().getExtras()
                .getParcelable(EXTRA_RECIPIENT_ADDRESS_DATA);
        Fragment fragment = MultipleAddressFragment.newInstance(
                cartListData,
                addressModel
        );
        String backStateName = fragment.getClass().getName();
        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!isFragmentPopped) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(backStateName)
                    .commit();
        }

//        FragmentManager fragmentManager = getFragmentManager();
//        List<CartSellerItemModel> cartSellerItemModels = getIntent()
//                .getExtras()
//                .getParcelableArrayList(CART_ITEM_LIST_EXTRA);
//        RecipientAddressModel addressModel = getIntent().getExtras().getParcelable(ADDRESS_MODEL);
//        Fragment fragment = MultipleAddressFragment.newInstance(
//                cartSellerItemModels,
//                addressModel
//        );
//
//        String backStateName = fragment.getClass().getName();
//
//        boolean isFragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
//        if (!isFragmentPopped) {
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .addToBackStack(backStateName)
//                    .commit();
//        }
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
}
