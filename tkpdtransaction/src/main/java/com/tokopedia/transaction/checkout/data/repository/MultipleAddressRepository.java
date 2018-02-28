package com.tokopedia.transaction.checkout.data.repository;

import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class MultipleAddressRepository implements IMultipleAddressRepository{

    public MultipleAddressRepository() {
    }

    @Override
    public Observable<String> sendMultipleAddressData(JsonObject dataToSend) {
        return null;
    }
}
