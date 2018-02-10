package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressRepository {

    Observable<String> sendMultipleAddressData(JsonObject dataToSend);

}
