package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartListInteractor implements ICartListInteractor {

    private final CompositeSubscription compositeSubscription;
    private final ICartRepository cartRepository;

    @Inject
    public CartListInteractor(CompositeSubscription compositeSubscription, ICartRepository cartRepository) {
        this.compositeSubscription = compositeSubscription;
        this.cartRepository = cartRepository;
    }


    @Override
    public void getCartList(Subscriber<String> subscriber, TKPDMapParam<String, String> param) {
        compositeSubscription.add(
                cartRepository.getCartList(param)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber)
        );
    }
}
