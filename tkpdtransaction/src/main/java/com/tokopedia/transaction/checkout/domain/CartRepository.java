package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.apiservice.CartService;

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
    public Observable<String> getCartList(TKPDMapParam<String, String> param) {
        return cartService.getApi().getCartListString(param).map(new Func1<Response<String>, String>() {
            @Override
            public String call(Response<String> stringResponse) {
                return stringResponse.body();
            }
        });
    }
}
