package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

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

public class MultipleAddressFormActivity extends BasePresenterActivity {
    //TODO HAPUS
    public static final String CART_ITEM_LIST_EXTRA = "CART_ITEM_LIST";
    public static final String ADDRESS_MODEL = "ADDRESS_MODEL";

    public static final int REQUEST_CODE = MultipleAddressFormActivity.class.hashCode();
    private static final String EXTRA_CART_LIST_DATA = "EXTRA_CART_LIST_DATA";
    private static final String EXTRA_RECIPIENT_ADDRESS_DATA = "EXTRA_RECIPIENT_ADDRESS_DATA";

    private CartListData cartListData;
    private RecipientAddressModel addressData;

    //TODO HAPUS
    public static Intent createInstance(Context context,
                                        List<CartSellerItemModel> cartSellerItemModels,
                                        RecipientAddressModel recipientModel) {
        Intent intent = new Intent(context, MultipleAddressFormActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(
                CART_ITEM_LIST_EXTRA,
                (ArrayList<? extends Parcelable>) cartSellerItemModels
        );
        bundle.putParcelable(ADDRESS_MODEL, recipientModel);
        intent.putExtras(bundle);
        return intent;
    }

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
        //TODO untuk cart list gunakan cartListData, untuk alamat guenakan addressData;

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
