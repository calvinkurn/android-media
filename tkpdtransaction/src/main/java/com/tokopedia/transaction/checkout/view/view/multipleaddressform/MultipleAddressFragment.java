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
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.view.adapter.MultipleAddressAdapter;
import com.tokopedia.transaction.checkout.view.di.component.DaggerMultipleAddressComponent;
import com.tokopedia.transaction.checkout.view.di.component.MultipleAddressComponent;
import com.tokopedia.transaction.checkout.view.di.module.MultipleAddressModule;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ICartShipmentActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressFragment.ADD_MODE;
import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressFragment.EDIT_MODE;
import static com.tokopedia.transaction.checkout.view.view.multipleaddressform.MultipleAddressFormActivity.RESULT_CODE_SUCCESS_SET_SHIPPING;

/**
 * Created by kris on 1/24/18. Tokopedia
 */

public class MultipleAddressFragment extends TkpdFragment
        implements IMultipleAddressView,
        MultipleAddressAdapter.MultipleAddressAdapterListener {

    @Inject
    IMultipleAddressPresenter presenter;

    private ICartShipmentActivity cartShipmentActivity;

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

    private MultipleAddressItemData dummyItemData() {
        MultipleAddressItemData data = new MultipleAddressItemData();
        data.setCartId("2");
        data.setAddressId("654321");
        data.setProductId("123456");
        data.setProductWeight("3Kg");
        data.setProductQty("1");
        data.setProductNotes("Saya pesan warna merah yah min.. jangan sampai salah\n" +
                "kirim barangnya gan!");
        data.setAddressTitle("Alamat Kantor");
        data.setAddressReceiverName("Agus Maulana");
        data.setAddress("Jl. Letjen S. Parman Kav.77, Wisma 77 Tower 2,\n" +
                "Tokopedia Lt. 2, Jakarta, 0817 1234 5678");
        return data;
    }

    private MultipleAddressAdapterData dummyAdapterData() {
        MultipleAddressAdapterData data = new MultipleAddressAdapterData();
        data.setSenderName("Adidas");
        data.setProductImageUrl("https://t00.deviantart.net/Qgvu_0dClD_BotaDpLBflGKcvbI=/300x200/filters:fixed_height(100,100):origin()/pre00/69b2/th/pre/f/2013/143/9/1/pusheen_the_cat_png_15_by_13taylorswiftlover13-d66chev.png");
        data.setProductName("Kaos Adidas Camo Tongue Tee...White & Red, XS");
        data.setProductPrice("Rp200.000");
        List<MultipleAddressItemData> itemDataList = new ArrayList<>();
        itemDataList.add(dummyItemData());
        itemDataList.add(dummyItemData());
        data.setItemListData(itemDataList);
        return data;
    }

    private List<MultipleAddressAdapterData> dummyDataList() {
        List<MultipleAddressAdapterData> list = new ArrayList<>();
        list.add(dummyAdapterData());
        list.add(dummyAdapterData());
        return list;
    }

    private List<MultipleAddressAdapterData> initiateAdapterData() {
        CartListData cartListData = getArguments()
                .getParcelable(CART_LIST_DATA);
        List<CartItemData> cartItemDataList = cartListData.getCartItemDataList();
        List<MultipleAddressAdapterData> adapterModels = new ArrayList<>();
        for (int i = 0; i < cartItemDataList.size(); i++) {
            MultipleAddressAdapterData addressAdapterData = new MultipleAddressAdapterData();
            addressAdapterData.setItemListData(
                    generateinitiatialItemData(
                            cartItemDataList.get(i).getOriginData(),
                            cartItemDataList.get(i).getUpdatedData())
            );
            addressAdapterData.setProductImageUrl(
                    cartItemDataList.get(i).getOriginData().getProductImage()
            );
            addressAdapterData.setProductName(
                    cartItemDataList.get(i).getOriginData().getProductName()
            );
            addressAdapterData.setProductPrice(
                    cartItemDataList.get(i).getOriginData().getPriceFormatted()
            );
            addressAdapterData.setSenderName(cartItemDataList.get(i).getOriginData().getShopName());
            adapterModels.add(addressAdapterData);
        }
        return adapterModels;
    }

    private List<MultipleAddressItemData> generateinitiatialItemData(
            CartItemData.OriginData originData,
            CartItemData.UpdatedData updatedData) {
        List<MultipleAddressItemData> initialItemData = new ArrayList<>();
        MultipleAddressItemData addressData = new MultipleAddressItemData();
        RecipientAddressModel shipmentRecipientModel = getArguments().getParcelable(ADDRESS_EXTRA);
        addressData.setCartId(String.valueOf(originData.getCartId()));
        addressData.setProductId(originData.getProductId());
        addressData.setProductQty(String.valueOf(updatedData.getQuantity()));
        addressData.setProductWeight(String.valueOf(originData.getWeightFormatted()));
        addressData.setProductNotes(updatedData.getRemark());
        addressData.setAddressId(shipmentRecipientModel.getId());
        addressData.setAddressTitle(shipmentRecipientModel.getRecipientName());
        addressData.setAddressReceiverName(shipmentRecipientModel.getRecipientName());
        addressData.setAddress(shipmentRecipientModel.getAddressStreet()
                + ", "
                + shipmentRecipientModel.getAddressCityName()
                + ", "
                + shipmentRecipientModel.getRecipientPhoneNumber());
        addressData.setMaxQuantity(updatedData.getMaxQuantity());
        addressData.setMinQuantity(originData.getMinimalQtyOrder());
        initialItemData.add(addressData);
        return initialItemData;
    }

    @Override
    public void onGoToChooseCourier(List<MultipleAddressAdapterData> dataList) {
        //TODO release later
        presenter.sendData(getActivity(), dataList);
    }

    @Override
    public void onAddNewShipmentAddress(MultipleAddressAdapterData data,
                                        MultipleAddressItemData addressData) {
        AddShipmentAddressFragment fragment = AddShipmentAddressFragment.newInstance(
                data,
                addressData,
                ADD_MODE);
        fragment.setTargetFragment(this, ADD_SHIPMENT_ADDRESS_REQUEST_CODE);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_left)
                .replace(R.id.container, fragment, ADD_SHIPMENT_FRAGMENT_TAG)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void onItemChoosen(MultipleAddressAdapterData productData,
                              MultipleAddressItemData addressData) {
        AddShipmentAddressFragment fragment = AddShipmentAddressFragment.newInstance(
                productData,
                addressData,
                EDIT_MODE);
        fragment.setTargetFragment(this, EDIT_SHIPMENT_ADDRESS_REQUEST_CODE);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_left)
                .replace(R.id.container, fragment, ADD_SHIPMENT_FRAGMENT_TAG)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_SHIPMENT_ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            multipleAddressAdapter.notifyDataSetChanged();
            removeAddAddressFragment();
        } else if (requestCode == ADD_SHIPMENT_ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            multipleAddressAdapter.notifyDataSetChanged();
            removeAddAddressFragment();
        }
    }

    private void removeAddAddressFragment() {
        getFragmentManager()
                .beginTransaction()
                .remove(getFragmentManager().findFragmentByTag(ADD_SHIPMENT_FRAGMENT_TAG))
                .commit();
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
