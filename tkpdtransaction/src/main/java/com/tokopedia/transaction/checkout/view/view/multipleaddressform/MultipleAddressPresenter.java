package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tokopedia.transaction.checkout.data.entity.request.MultipleAddressRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.transaction.checkout.domain.usecase.SubmitMultipleAddressUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressPresenter implements IMultipleAddressPresenter {

    private SubmitMultipleAddressUseCase submitMultipleAddressUseCase;

    private IMultipleAddressView view;

    public MultipleAddressPresenter(IMultipleAddressView view, SubmitMultipleAddressUseCase submitMultipleAddressUseCase) {
        this.submitMultipleAddressUseCase = submitMultipleAddressUseCase;
        this.view = view;
    }

    @Override
    public void sendData(Context context, List<MultipleAddressAdapterData> dataList) {
        JsonArray dataArray = new JsonArray();
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j < dataList.get(i).getItemListData().size(); j++) {
                MultipleAddressRequest request = new MultipleAddressRequest();
                MultipleAddressItemData itemData = dataList.get(i).getItemListData().get(j);
                request.setCartId(Integer.parseInt(itemData.getCartId()));
                request.setProductId(Integer.parseInt(itemData.getProductId()));
                request.setAddressId(Integer.parseInt(itemData.getAddressId()));
                request.setNotes(itemData.getProductNotes());
                request.setQuantity(Integer.parseInt(itemData.getProductQty()));
                JsonElement cartData = new JsonParser().parse(new Gson().toJson(request));
                dataArray.add(cartData);
            }
        }
        RequestParams requestParam = RequestParams.create();
        requestParam.putString("carts", dataArray.toString());
        submitMultipleAddressUseCase.execute(
                requestParam,
                addMultipleAddressSubscriber());
    }

    @Override
    public List<MultipleAddressAdapterData> initiateMultipleAddressAdapterData(
            CartListData cartListData,
            RecipientAddressModel recipientAddressModel) {
        List<CartItemData> cartItemDataList = cartListData.getCartItemDataList();
        List<MultipleAddressAdapterData> adapterModels = new ArrayList<>();
        for (int i = 0; i < cartItemDataList.size(); i++) {
            MultipleAddressAdapterData addressAdapterData = new MultipleAddressAdapterData();
            addressAdapterData.setItemListData(
                    generateinitialItemData(
                            i,
                            recipientAddressModel,
                            cartItemDataList.get(i).getOriginData(),
                            cartItemDataList.get(i).getUpdatedData(),
                            cartItemDataList.get(i).getErrorData())
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

    private List<MultipleAddressItemData> generateinitialItemData(
            int cartPosition,
            RecipientAddressModel shipmentRecipientModel,
            CartItemData.OriginData originData,
            CartItemData.UpdatedData updatedData,
            CartItemData.MessageErrorData messageErrorData) {

        List<MultipleAddressItemData> initialItemData = new ArrayList<>();
        MultipleAddressItemData addressData = new MultipleAddressItemData();
        addressData.setCartPosition(cartPosition);
        addressData.setAddressPosition(0);
        addressData.setCartId(String.valueOf(originData.getCartId()));
        addressData.setProductId(originData.getProductId());
        addressData.setProductQty(String.valueOf(updatedData.getQuantity()));
        addressData.setProductWeight(String.valueOf(originData.getWeightFormatted()));
        addressData.setProductNotes(updatedData.getRemark());
        addressData.setAddressId(shipmentRecipientModel.getId());
        addressData.setAddressTitle(shipmentRecipientModel.getRecipientName());
        addressData.setAddressReceiverName(shipmentRecipientModel.getRecipientName());
        addressData.setAddressProvinceName(shipmentRecipientModel.getAddressProvinceName());
        addressData.setAddressPostalCode(shipmentRecipientModel.getAddressPostalCode());
        addressData.setAddressCityName(shipmentRecipientModel.getAddressCityName());
        addressData.setAddressStreet(shipmentRecipientModel.getAddressStreet());
        addressData.setAddressCountryName(shipmentRecipientModel.getAddressCountryName());
        addressData.setRecipientPhoneNumber(shipmentRecipientModel.getRecipientPhoneNumber());
        addressData.setDestinationDistrictId(shipmentRecipientModel.getDestinationDistrictId());
        addressData.setDestinationDistrictName(shipmentRecipientModel.getDestinationDistrictName());
        addressData.setMaxQuantity(updatedData.getMaxQuantity());
        addressData.setMinQuantity(originData.getMinimalQtyOrder());
        addressData.setErrorCheckoutPriceLimit(messageErrorData.getErrorCheckoutPriceLimit());
        addressData.setErrorFieldBetween(messageErrorData.getErrorFieldBetween());
        addressData.setErrorFieldMaxChar(messageErrorData.getErrorFieldMaxChar());
        addressData.setErrorProductAvailableStock(messageErrorData.getErrorProductAvailableStock());
        addressData.setErrorProductAvailableStockDetail(messageErrorData.getErrorProductAvailableStockDetail());
        addressData.setErrorProductMaxQuantity(messageErrorData.getErrorProductMaxQuantity());
        addressData.setErrorProductMinQuantity(messageErrorData.getErrorProductMinQuantity());
        addressData.setMaxRemark(updatedData.getMaxCharRemark());
        initialItemData.add(addressData);
        return initialItemData;
    }

    @Override
    public void onUnsubscribe() {
        submitMultipleAddressUseCase.unsubscribe();
    }

    private Subscriber<SetShippingAddressData> addMultipleAddressSubscriber() {
        return new Subscriber<SetShippingAddressData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SetShippingAddressData setShippingAddressData) {
                if (setShippingAddressData.isSuccess())
                    view.successMakeShipmentData();
            }
        };
    }

}
