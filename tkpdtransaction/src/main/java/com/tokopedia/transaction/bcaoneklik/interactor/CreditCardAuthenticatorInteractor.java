package com.tokopedia.transaction.bcaoneklik.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorUpdateWhiteListResponse;

import rx.Subscriber;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

public interface CreditCardAuthenticatorInteractor {

    void updateAuthenticationStatus(JsonObject requestBody,
                                    Subscriber<AuthenticatorUpdateWhiteListResponse> subscriber);

}
