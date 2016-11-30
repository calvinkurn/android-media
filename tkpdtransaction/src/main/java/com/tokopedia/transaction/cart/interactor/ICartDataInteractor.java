package com.tokopedia.transaction.cart.interactor;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.cartdata.CartModel;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         collabs with alvarisi
 */

public interface ICartDataInteractor {

    void getCartData(TKPDMapParam<String, String> param,
                     Subscriber<CartModel> subscriber);

    void cancelCart(TKPDMapParam<String, String> paramCancelCart,
                    TKPDMapParam<String, String> paramCartInfo,
                    Subscriber<CartModel> subscriber);

    void calculateCart(TKPDMapParam<String, String> param, Subscriber<Object> subscriber);

    void calculateShipment(TKPDMapParam<String, String> param, Subscriber<List<Shipment>> subscriber);

    void editShipmentCart(TKPDMapParam<String, String> param, Subscriber<EditShipmentCart> subscriber);

    void editLocationShipment(TKPDMapParam<String, String> param, Subscriber<SaveLocationData> subscriber);

    void updateCart(TKPDMapParam<String, String> paramUpdate,
                    TKPDMapParam<String, String> paramCart, Subscriber<CartModel> subscriber);

    void updateInsuranceCart(TKPDMapParam<String, String> paramUpdate,
                             TKPDMapParam<String, String> paramCart,
                             Subscriber<CartModel> subscriber);

    void getParameterTopPay(TKPDMapParam<String, String> params, Scheduler schedulers,
                            Subscriber<TopPayParameterData> subscriber);

    void unSubscribeObservable();
}
