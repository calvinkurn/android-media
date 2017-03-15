package com.tokopedia.payment.cart.presenter;

import com.tokopedia.payment.model.PaymentPassData;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public interface ITopPayPresenter {

    String CHARSET_UTF_8 = "UTF-8";

    void proccessUriPayment(PaymentPassData paymentPassData);

    void processVerifyPaymentIdByCancelTopPay(String paymentId);

    PaymentPassData getPaymentPassData();

    void setPaymentPassData(PaymentPassData paymentPassData);

    String getCurrentPaymentId();

}
