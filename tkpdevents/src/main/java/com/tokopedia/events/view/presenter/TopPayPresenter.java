package com.tokopedia.events.view.presenter;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.events.view.contractor.TopPayContractor;
import com.tokopedia.events.view.viewmodel.PaymentPassData;

import java.io.UnsupportedEncodingException;

/**
 * Created by pranaymohapatra on 15/12/17.
 */

public class TopPayPresenter implements TopPayContractor.Presenter {

    private final TopPayContractor.ITopPayView view;

    public TopPayPresenter(TopPayContractor.ITopPayView view) {
        this.view = view;
    }

    @Override
    public void proccessUriPayment() {
        PaymentPassData paymentPassData = view.getPaymentPassData();
        try {
            byte[] postData = paymentPassData.getQueryString().getBytes("UTF-8");
            view.renderWebViewPostUrl(paymentPassData.getRedirectUrl(), postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void attachView(TopPayContractor.ITopPayView view) {

    }

    @Override
    public void detachView() {

    }
}
