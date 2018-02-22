package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.apiservice.CartResponse;
import com.tokopedia.transaction.apiservice.CartService;
import com.tokopedia.transaction.checkout.domain.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transaction.checkout.domain.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.domain.response.updatecart.UpdateCartDataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartRepository implements ICartRepository {

    private CartService cartService;

    @Inject
    public CartRepository(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public Observable<CartDataListResponse> getCartList(TKPDMapParam<String, String> param) {
        return cartService.getApi().getCartList(param).map(
                new Func1<Response<CartResponse>, CartDataListResponse>() {
                    @Override
                    public CartDataListResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(CartDataListResponse.class);
                    }
                });
    }

    @Override
    public Observable<DeleteCartDataResponse> deleteCartData(TKPDMapParam<String, String> param) {
        return cartService.getApi().postDeleteCart(param).map(
                new Func1<Response<CartResponse>, DeleteCartDataResponse>() {
                    @Override
                    public DeleteCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(DeleteCartDataResponse.class);
                    }
                });
    }

    @Override
    public Observable<AddToCartDataResponse> addToCartData(TKPDMapParam<String, String> param) {
        return cartService.getApi().postAddToCart(param).map(
                new Func1<Response<CartResponse>, AddToCartDataResponse>() {
                    @Override
                    public AddToCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(AddToCartDataResponse.class);
                    }
                }
        );
    }

    @Override
    public Observable<UpdateCartDataResponse> updateCartData(TKPDMapParam<String, String> param) {
        return cartService.getApi().postUpdateCart(param).map(new Func1<Response<CartResponse>, UpdateCartDataResponse>() {
            @Override
            public UpdateCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(UpdateCartDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ShippingAddressDataResponse> shippingAddress(TKPDMapParam<String, String> param) {
        return cartService.getApi().postSetShippingAddress(param).map(new Func1<Response<CartResponse>, ShippingAddressDataResponse>() {
            @Override
            public ShippingAddressDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ShippingAddressDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(TKPDMapParam<String, String> param) {
        return cartService.getApi().getShipmentAddressForm(param).map(new Func1<Response<CartResponse>, ShipmentAddressFormDataResponse>() {
            @Override
            public ShipmentAddressFormDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ShipmentAddressFormDataResponse.class);
            }
        });
    }


}
