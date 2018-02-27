package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.CartSellerItemModel;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 2/22/18. Tokopedia
 */

public class MultipleAddressFormActivity extends BasePresenterActivity{

    public static final String CART_DATA = "CART_DATA";
    public static final String ADDRESS_MODEL = "ADDRESS_MODEL";

    public static Intent createInstance(Context context,
                                             CartListData cartListData,
                                             RecipientAddressModel recipientModel) {
        Intent intent = new Intent(context, MultipleAddressFormActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADDRESS_MODEL, recipientModel);
        bundle.putParcelable(CART_DATA, cartListData);
        intent.putExtras(bundle);
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
        return R.layout.multiple_address_form_activity;
    }

    @Override
    protected void initView() {
        FragmentManager fragmentManager = getFragmentManager();
        CartListData cartListData = getIntent()
                .getExtras()
                .getParcelable(CART_DATA);
        RecipientAddressModel addressModel = getIntent().getExtras().getParcelable(ADDRESS_MODEL);
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
