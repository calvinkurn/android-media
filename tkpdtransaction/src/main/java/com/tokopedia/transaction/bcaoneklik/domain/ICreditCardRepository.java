package com.tokopedia.transaction.bcaoneklik.domain;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorCheckWhiteListResponse;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorLogicModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public interface ICreditCardRepository {

    Observable<CreditCardModel> getCreditCardList(JsonObject requestBody);

    Observable<CreditCardSuccessDeleteModel> deleteCreditCard(JsonObject requestBody);

    Observable<AuthenticatorPageModel> checkCreditCardWhiteList(JsonObject object);

    Observable<AuthenticatorUpdateWhiteListResponse> updateCreditCardWhiteList(JsonObject object);
}
