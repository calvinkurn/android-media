package com.tokopedia.transaction.checkout.domain.usecase;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.checkout.data.repository.ICartRepository;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressInteractor implements IMultipleAddressInteractor {

    private CompositeSubscription compositeSubscription;

    private ICartRepository repository;

    public MultipleAddressInteractor(CompositeSubscription compositeSubscription,
                                     ICartRepository repository) {
        this.compositeSubscription = compositeSubscription;
        this.repository = repository;
    }

    @Override
    public void sendAddressData(JsonObject dataToSend,
                                Subscriber<String> subscriber) {

    }

}
