package com.tokopedia.transaction.checkout.view.view.multipleaddressform;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.tokopedia.transaction.checkout.data.entity.request.MultipleAddressRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressItemData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.transaction.checkout.domain.usecase.SubmitMultipleAddressUseCase;
import com.tokopedia.usecase.RequestParams;

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
