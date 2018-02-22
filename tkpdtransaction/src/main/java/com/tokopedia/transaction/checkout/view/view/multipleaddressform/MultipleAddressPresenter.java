package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.transaction.checkout.domain.IMultipleAddressInteractor;
import com.tokopedia.transaction.checkout.domain.request.MultipleAddressRequest;
import com.tokopedia.transaction.checkout.domain.request.MultipleAddressRequestList;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressItemData;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressPresenter implements IMultipleAddressPresenter {

    private IMultipleAddressInteractor interactor;

    public MultipleAddressPresenter(IMultipleAddressInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void sendData(List<MultipleAddressAdapterData> dataList) {
        JsonObject dataToSend = new JsonObject();
        MultipleAddressRequestList multipleAddressRequestList = new MultipleAddressRequestList();
        List<MultipleAddressRequest> requestList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            for (int j = 0; j<dataList.get(i).getItemListData().size(); j++) {
                MultipleAddressRequest request = new MultipleAddressRequest();
                MultipleAddressItemData itemData = dataList.get(i).getItemListData().get(j);
                request.setCartId(Integer.parseInt(itemData.getCartId()));
                request.setProductId(Integer.parseInt(itemData.getProductId()));
                request.setAddressId(Integer.parseInt(itemData.getAddressId()));
                request.setNotes(itemData.getProductNotes());
                request.setQuantity(Integer.parseInt(itemData.getProductQty()));
                requestList.add(request);
            }
            multipleAddressRequestList.setAddressRequests(requestList);
        }
        dataToSend = new JsonParser().parse(new Gson()
                .toJson(multipleAddressRequestList)).getAsJsonObject();

        //TODO release later
        //interactor.sendAddressData(dataToSend, addMultipleAddressSubscriber());

        CommonUtils.dumper("PORING data to send " + dataToSend.toString());
    }

    private Subscriber<String> addMultipleAddressSubscriber() {
        return new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
    }

}
