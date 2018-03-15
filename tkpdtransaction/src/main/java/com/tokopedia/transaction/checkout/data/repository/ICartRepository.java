package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.data.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.updatecart.UpdateCartDataResponse;

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

    Observable<CheckPromoCodeCartListDataResponse> checkPromoCodeCartList(TKPDMapParam<String, String> param);

    Observable<CheckPromoCodeFinalDataResponse> checkPromoCodeCartShipment(TKPDMapParam<String, String> param);

    Observable<CouponDataResponse> getCouponList(TKPDMapParam<String, String> param);

}
