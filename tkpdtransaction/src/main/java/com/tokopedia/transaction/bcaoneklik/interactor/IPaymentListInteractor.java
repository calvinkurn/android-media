package com.tokopedia.transaction.bcaoneklik.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenter;

import rx.Subscriber;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public interface IPaymentListInteractor {

    void getPaymentList(Subscriber<CreditCardModel> subscriber,
                        JsonObject requestBody);

    void deleteCreditCard(Subscriber<CreditCardSuccessDeleteModel> subscriber,
                          JsonObject requestBody);
}
