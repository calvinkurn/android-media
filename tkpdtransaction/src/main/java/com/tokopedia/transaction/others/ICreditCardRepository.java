package com.tokopedia.transaction.others;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.others.creditcard.CreditCardModel;
import com.tokopedia.transaction.others.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.others.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.others.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Observable;

public interface ICreditCardRepository {
    Observable<CreditCardModel> getCreditCardList(JsonObject requestBody);

    Observable<CreditCardSuccessDeleteModel> deleteCreditCard(JsonObject requestBody);

    Observable<AuthenticatorPageModel> checkCreditCardWhiteList(JsonObject object);

    Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject object);
}
