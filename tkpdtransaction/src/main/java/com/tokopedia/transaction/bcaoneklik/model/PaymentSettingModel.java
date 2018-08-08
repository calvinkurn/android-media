package com.tokopedia.transaction.bcaoneklik.model;

import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModel;

/**
 * Created by kris on 10/4/17. Tokopedia
 */

public class PaymentSettingModel {

    private PaymentListModel paymentListModel;

    private CreditCardModel creditCardResponse;

    public PaymentListModel getBcaOneClickModel() {
        return paymentListModel;
    }

    public void setBcaOneClickModel(PaymentListModel paymentListModel) {
        this.paymentListModel = paymentListModel;
    }

    public CreditCardModel getCreditCardResponse() {
        return creditCardResponse;
    }

    public void setCreditCardResponse(CreditCardModel creditCardResponse) {
        this.creditCardResponse = creditCardResponse;
    }
}
