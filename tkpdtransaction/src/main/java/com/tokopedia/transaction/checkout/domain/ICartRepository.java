package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.checkout.CheckoutDataResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.domain.response.updatecart.UpdateCartDataResponse;

import rx.Observable;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartRepository {

    Observable<CartDataListResponse> getCartList(TKPDMapParam<String, String> param);

    Observable<DeleteCartDataResponse> deleteCartData(TKPDMapParam<String, String> param);

    Observable<AddToCartDataResponse> addToCartData(TKPDMapParam<String, String> param);

    Observable<UpdateCartDataResponse> updateCartData(TKPDMapParam<String, String> param);

    Observable<ShippingAddressDataResponse> shippingAddress(TKPDMapParam<String, String> param);

    Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(TKPDMapParam<String, String> param);

    Observable<ResetCartDataResponse> resetCart(TKPDMapParam<String, String> param);

    Observable<CheckoutDataResponse> checkout(TKPDMapParam<String, String> param);

}
