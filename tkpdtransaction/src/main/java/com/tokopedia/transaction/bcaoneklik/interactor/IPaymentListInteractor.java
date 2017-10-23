package com.tokopedia.transaction.bcaoneklik.interactor;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickData;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardSuccessDeleteModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;

import rx.Subscriber;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

public interface IPaymentListInteractor {

    void getAllPaymentList(Subscriber<PaymentSettingModel> subscriber,
                           JsonObject creditCardListRequest,
                           TKPDMapParam<String, String> bcaOneClickParams);

    void getPaymentList(Subscriber<CreditCardModel> subscriber,
                        JsonObject requestBody);

    void getBcaOneClickList(Subscriber<PaymentListModel> subscriber,
                            TKPDMapParam<String, String> bcaOneClickParam);

    void deleteCreditCard(Subscriber<CreditCardSuccessDeleteModel> subscriber,
                          JsonObject requestBody);

    void getBcaOneClickAccessToken(Subscriber<BcaOneClickData> subscriber,
                             TKPDMapParam<String, String> registrationParams);

    void deleteBcaOneClick(Subscriber<PaymentListModel> subscriber,
                           TKPDMapParam<String, String> deleteBcaOneClickParam);

    void checkCreditCardWhiteList(Subscriber<AuthenticatorPageModel> subscriber,
                                  TKPDMapParam<String, String> userInfoParams);

    void onActivityDestroyed();
}
