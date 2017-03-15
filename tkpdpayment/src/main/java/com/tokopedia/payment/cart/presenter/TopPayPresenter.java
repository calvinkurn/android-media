package com.tokopedia.payment.cart.presenter;

import com.tokopedia.payment.cart.listener.ITopPayView;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public class TopPayPresenter implements ITopPayPresenter{

    private final ITopPayView view;

    private String paymentId;

    private PaymentPassData paymentPassData;

    public TopPayPresenter(ITopPayView view) {
        this.view = view;
    }

    @Override
    public void proccessUriPayment(PaymentPassData paymentPassData) {
        try {
            byte[] postData = paymentPassData.getQueryString().getBytes(CHARSET_UTF_8);
            view.renderWebViewPostUrl(paymentPassData.getRedirectUrl(), postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void processVerifyPaymentIdByCancelTopPay(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public PaymentPassData getPaymentPassData() {
        return paymentPassData;
    }

    @Override
    public void setPaymentPassData(PaymentPassData paymentPassData) {
        this.paymentPassData = paymentPassData;
    }

    @Override
    public String getCurrentPaymentId() {
        return paymentId;
    }

}
