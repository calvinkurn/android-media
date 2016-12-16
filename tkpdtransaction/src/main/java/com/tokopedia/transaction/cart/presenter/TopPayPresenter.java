package com.tokopedia.transaction.cart.presenter;

import android.content.Intent;
import android.net.Uri;

import com.tokopedia.transaction.cart.listener.ITopPayView;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.services.TopPayIntentService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author anggaprasetiyo on 12/16/16.
 */

public class TopPayPresenter implements ITopPayPresenter {

    private final ITopPayView view;

    public TopPayPresenter(ITopPayView view) {
        this.view = view;
    }

    @Override
    public void proccessUriPayment(TopPayParameterData topPayParameterData) {
        try {
            byte[] postData = topPayParameterData.getQueryString().getBytes(CHARSET_UTF_8);
            view.renderWebViewPostUrl(topPayParameterData.getRedirectUrl(), postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            view.showToastMessageWithForceCloseView("Terjadi Kesalahan, Silahkan coba kembali");
        }
    }

    @Override
    public void processRedirectUrlContainsTopPayCallbackUrl(String url) {
        Uri uri = Uri.parse(url);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        view.setPaymentId(paymentId);
        processVerifyPaymentId(paymentId);
    }

    @Override
    public void processRedirectUrlContainsAccountUrl(String url) {
        Uri uriMain = Uri.parse(url);
        String ld = uriMain.getQueryParameter(KEY_QUERY_LD);
        String urlThanks;
        try {
            urlThanks = URLDecoder.decode(ld, CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            urlThanks = "";
        }
        Uri uri = Uri.parse(urlThanks);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        view.setPaymentId(paymentId);
        processVerifyPaymentId(paymentId);
    }

    @Override
    public void processRedirectUrlContainsLoginUrl() {
        view.showToastMessageWithForceCloseView("Proses pembayaran gagal, mohon ulangi lagi");
    }

    @Override
    public void processVerifyPaymentId(String paymentId) {
        if (paymentId == null) {
            view.showToastMessageWithForceCloseView("Proses pembayaran gagal, mohon ulangi lagi");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SYNC, null, view.getContextActivity(),
                TopPayIntentService.class);
        intent.putExtra(TopPayIntentService.EXTRA_ACTION,
                TopPayIntentService.SERVICE_ACTION_GET_THANKS_TOP_PAY);
        intent.putExtra(TopPayIntentService.EXTRA_PAYMENT_ID, paymentId);
        view.executeService(intent);
    }
}
