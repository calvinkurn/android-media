package com.tokopedia.transaction.bcaoneklik.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.transaction.CreditCardVaultService;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;

import rx.Observable;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public interface ICreditCardRepository {

    Observable<CreditCardModel> getCreditCardList(CreditCardVaultService service,
                                                  JsonObject requestBody);

    Observable<CreditCardSuccessDeleteModel> deleteCreditCard(CreditCardVaultService service,
                                                              JsonObject requestBody);
}
