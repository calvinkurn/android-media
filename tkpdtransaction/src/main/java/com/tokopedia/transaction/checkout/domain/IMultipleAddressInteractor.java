package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressInteractor {

    void sendAddressData(JsonObject dataToSend, Subscriber<String> subscriber);

}
