package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressInteractor implements IMultipleAddressInteractor{

    private CompositeSubscription compositeSubscription;

    private IMultipleAddressRepository repository;

    public MultipleAddressInteractor(CompositeSubscription compositeSubscription,
                                     IMultipleAddressRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.repository = repository;
    }

    @Override
    public void sendAddressData(JsonObject dataToSend,
                                Subscriber<String> subscriber) {
        compositeSubscription.add(repository.sendMultipleAddressData(dataToSend)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

}
