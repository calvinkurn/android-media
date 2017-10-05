package com.tokopedia.payment.presenter;

import com.tokopedia.payment.listener.ITopPayView;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public class TopPayPresenter implements ITopPayPresenter {

    private final ITopPayView view;

    public TopPayPresenter(ITopPayView view) {
        this.view = view;
    }

    @Override
    public void proccessUriPayment() {
        PaymentPassData paymentPassData = view.getPaymentPassData();
        try {
            byte[] postData = paymentPassData.getQueryString().getBytes(CHARSET_UTF_8);
            view.renderWebViewPostUrl(paymentPassData.getRedirectUrl(), postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

}
