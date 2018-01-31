package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.apiservice.CartResponse;
import com.tokopedia.transaction.apiservice.CartService;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;

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
        return cartService.getApi().getCartList(param).map(new Func1<Response<CartResponse>, CartDataListResponse>() {
            @Override
            public CartDataListResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(CartDataListResponse.class);
            }
        });
    }

    @Override
    public Observable<DeleteCartDataResponse> deleteCartData(JsonObject param) {
        return cartService.getApi().postDeleteCart(param).map(new Func1<Response<CartResponse>, DeleteCartDataResponse>() {
            @Override
            public DeleteCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                return null;
            }
        });
    }
}
