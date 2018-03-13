package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerMultipleAddressComponent;
import com.tokopedia.transaction.checkout.view.di.component.MultipleAddressComponent;
import com.tokopedia.transaction.checkout.view.di.module.MultipleAddressModule;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.ADDRESS_DATA_RESULT;
import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.ADD_MODE;
import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressActivity.EDIT_MODE;
import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressFragment extends TkpdFragment
        implements IMultipleAddressView,
        MultipleAddressAdapter.MultipleAddressAdapterListener {

    @Inject
    IMultipleAddressPresenter presenter;

    public static final int ADD_SHIPMENT_ADDRESS_REQUEST_CODE = 21;
    public static final int EDIT_SHIPMENT_ADDRESS_REQUEST_CODE = 22;
    private static final String ADD_SHIPMENT_FRAGMENT_TAG = "ADD_SHIPMENT_FRAGMENT_TAG";
    private static final String CART_LIST_DATA = "CART_LIST_DATA";
    private static final String ADDRESS_EXTRA = "ADDRESS_EXTRA";

    private MultipleAddressAdapter multipleAddressAdapter;

    public static MultipleAddressFragment newInstance(
            CartListData cartListData,
            RecipientAddressModel recipientModel
    ) {
        MultipleAddressFragment fragment = new MultipleAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ADDRESS_EXTRA, recipientModel);
        bundle.putParcelable(CART_LIST_DATA, cartListData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.multiple_address_fragment, container, false);
        initInjector();
        RecyclerView orderAddressList = view.findViewById(R.id.order_address_list);
        orderAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));
        multipleAddressAdapter = new MultipleAddressAdapter(initiateAdapterData(), this);
        orderAddressList.setAdapter(multipleAddressAdapter);
        return view;
    }

    private void initInjector() {
        MultipleAddressComponent component = DaggerMultipleAddressComponent
                .builder()
                .multipleAddressModule(new MultipleAddressModule(this)).build();
        component.inject(this);
    }

    private List<MultipleAddressAdapterData> initiateAdapterData() {
        CartListData cartListData = getArguments().getParcelable(CART_LIST_DATA);
        RecipientAddressModel recipientAddressModel = getArguments().getParcelable(ADDRESS_EXTRA);
        return presenter.initiateMultipleAddressAdapterData(cartListData, recipientAddressModel);
    }

    @Override
    public void onGoToChooseCourier(List<MultipleAddressAdapterData> dataList) {
        presenter.sendData(getActivity(), dataList);

    }

    @Override
    public void onAddNewShipmentAddress(int addressPositionToAdd, MultipleAddressAdapterData data,
                                        MultipleAddressItemData addressData) {
        startActivityForResult(AddShipmentAddressActivity
                        .createIntent(getActivity(), data, addressData, ADD_MODE),
                ADD_SHIPMENT_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onItemChoosen(MultipleAddressAdapterData productData,
                              MultipleAddressItemData addressData) {
        startActivityForResult(AddShipmentAddressActivity
                        .createIntent(getActivity(),
                                productData,
                                addressData, EDIT_MODE),
                EDIT_SHIPMENT_ADDRESS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_SHIPMENT_ADDRESS_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            MultipleAddressItemData editedAddressData = data
                    .getParcelableExtra(ADDRESS_DATA_RESULT);
            multipleAddressAdapter.getAddressData()
                    .get(editedAddressData.getCartPosition())
                    .getItemListData()
                    .set(editedAddressData.getAddressPosition(), editedAddressData);
            multipleAddressAdapter.notifyDataSetChanged();
        } else if (requestCode == ADD_SHIPMENT_ADDRESS_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            MultipleAddressItemData editedAddressData = data
                    .getParcelableExtra(ADDRESS_DATA_RESULT);
            multipleAddressAdapter.getAddressData()
                    .get(editedAddressData.getCartPosition())
                    .getItemListData()
                    .add(editedAddressData.getAddressPosition(), editedAddressData);
            multipleAddressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void successMakeShipmentData() {
        getActivity().setResult(RESULT_CODE_SUCCESS_SET_SHIPPING);
        getActivity().finish();
    }

    @Override
    public void receiveData(List<MultipleAddressAdapterData> dataList) {

    }

    @Override
    public void onDestroyView() {
        presenter.onUnsubscribe();
        super.onDestroyView();
    }
}
