package com.tokopedia.transaction.others;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.others.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Observable;

public interface ICreditCardRepository {

    Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject object);
}
