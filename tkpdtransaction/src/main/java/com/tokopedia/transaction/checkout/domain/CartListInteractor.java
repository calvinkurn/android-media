package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartListInteractor implements ICartListInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ICartRepository cartRepository;
    private final ICartMapper mapper;

    @Inject
    public CartListInteractor(CompositeSubscription compositeSubscription,
                              ICartRepository cartRepository, ICartMapper mapper) {
        this.compositeSubscription = compositeSubscription;
        this.cartRepository = cartRepository;
        this.mapper = mapper;
    }


    @Override
    public void getCartList(Subscriber<List<CartItemData>> subscriber, TKPDMapParam<String, String> param) {
        compositeSubscription.add(
                cartRepository.getCartList(param)
                        .map(new Func1<CartDataListResponse, List<CartItemData>>() {
                            @Override
                            public List<CartItemData> call(CartDataListResponse cartDataListResponse) {
                                return mapper.convertToCartItemDataList(cartDataListResponse);
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }

    @Override
    public void deleteCart(Subscriber<String> subscriber, JsonObject param) {

    }
}
