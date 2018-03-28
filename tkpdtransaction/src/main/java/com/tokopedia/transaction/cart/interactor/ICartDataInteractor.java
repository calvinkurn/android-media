package com.tokopedia.transaction.cart.interactor;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.ResponseTransform;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.cartdata.CartData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartRatesData;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         collabs with alvarisi
 */

public interface ICartDataInteractor {

    void getCartData(TKPDMapParam<String, String> param,
                     Subscriber<ResponseTransform<CartData>> subscriber);

    void cancelCart(TKPDMapParam<String, String> paramCancelCart,
                    TKPDMapParam<String, String> paramCartInfo,
                    Subscriber<ResponseTransform<CartData>> subscriber);

    void calculateCart(TKPDMapParam<String, String> param, Subscriber<Object> subscriber);

    void calculateShipment(TKPDMapParam<String, String> param, Subscriber<List<Shipment>> subscriber);

    void editShipmentCart(TKPDMapParam<String, String> param, Subscriber<EditShipmentCart> subscriber);

    void editLocationShipment(TKPDMapParam<String, String> param,
                              Subscriber<SaveLocationData> subscriber);

    void updateCart(TKPDMapParam<String, String> paramUpdate,
                    TKPDMapParam<String, String> paramCart,
                    Subscriber<ResponseTransform<CartData>> subscriber);

    void updateInsuranceCart(TKPDMapParam<String, String> paramUpdate,
                             TKPDMapParam<String, String> paramCart,
                             Subscriber<ResponseTransform<CartData>> subscriber);

    void getParameterTopPay(TKPDMapParam<String, Object> params, Scheduler schedulers,
                            Subscriber<TopPayParameterData> subscriber);

    void getThanksTopPay(TKPDMapParam<String, String> params, Scheduler schedulers,
                         Subscriber<ThanksTopPayData> subscriber);

    void checkVoucherCode(TKPDMapParam<String, String> stringStringTKPDMapParam,
                          Subscriber<ResponseTransform<VoucherData>> subscriber);

    void unSubscribeObservable();

    void calculateKeroRates(String token, String ut,
                            List<CartItem> cartItemList,
                            KeroRatesListener listener);

    interface KeroRatesListener {
        void onSuccess(CartRatesData cartRatesData);

        void onAllDataCompleted();

        void onRatesFailed(String errorMessage);

        void onConnectionFailed();
    }
}
