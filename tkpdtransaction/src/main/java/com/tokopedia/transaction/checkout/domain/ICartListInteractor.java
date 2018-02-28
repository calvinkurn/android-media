package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;
import com.tokopedia.transaction.checkout.view.data.DeleteUpdateCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateCartData;
import com.tokopedia.transaction.checkout.view.data.UpdateToSingleAddressShipmentData;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.view.data.voucher.PromoCodeCartListData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartListInteractor {

    void getCartList(Subscriber<CartListData> subscriber, TKPDMapParam<String, String> param);

    void deleteCart(Subscriber<DeleteCartData> subscriber, TKPDMapParam<String, String> param);

    void deleteAndUpdateCart(Subscriber<DeleteUpdateCartData> subscriber,
                             TKPDMapParam<String, String> paramDelete,
                             TKPDMapParam<String, String> paramUpdate);

    void updateCart(Subscriber<UpdateCartData> subscriber, TKPDMapParam<String, String> param);

    void updateCartToSingleAddressShipment(Subscriber<UpdateToSingleAddressShipmentData> subscriber,
                                           TKPDMapParam<String, String> paramUpdate,
                                           TKPDMapParam<String, String> paramGetShipmentForm);

    void checkPromoCodeCartList(
            Subscriber<PromoCodeCartListData> subscriber,  TKPDMapParam<String, String> param
    );

    void getShipmentForm(Subscriber<CartShipmentAddressFormData> subscriber,
                         TKPDMapParam<String, String> param);
}
