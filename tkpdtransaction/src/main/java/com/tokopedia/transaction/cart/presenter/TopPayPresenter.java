package com.tokopedia.transaction.cart.presenter;

import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.cart.listener.ITopPayView;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.services.TopPayIntentService;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

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
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void processRedirectUrlContainsTopPayCallbackUrl(String url) {
        Uri uri = Uri.parse(url);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        view.setPaymentId(paymentId);
        processVerifyPaymentId();
    }

    @Override
    public void processRedirectUrlContainsTopPayRedirectUrl(String url) {
        Uri uri = Uri.parse(url);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        view.setPaymentId(paymentId);
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
        processVerifyPaymentId();
    }

    @Override
    public void processRedirectUrlContainsLoginUrl() {
        view.showToastMessageWithForceCloseView(
                view.getStringFromResource(R.string.label_error_default_top_pay)
        );
    }

    @Override
    public void processVerifyPaymentId() {
        Bundle bundle = new Bundle();
        bundle.putInt(TopPayIntentService.EXTRA_ACTION,
                TopPayIntentService.SERVICE_ACTION_GET_THANKS_TOP_PAY);
        bundle.putString(TopPayIntentService.EXTRA_PAYMENT_ID, view.getPaymentId());
        view.executeIntentService(bundle, TopPayIntentService.class);
    }

    @Override
    public void processVerifyPaymentIdByCancelTopPay(String paymentId) {
        processVerifyPaymentId();
    }

    @Override
    public void clearNotificationCart() {
        LocalCacheHandler cache = view.getLocalCacheHandlerNotification();
        cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
        cache.applyEditor();
    }

    @Override
    public void processPaymentAnalytics(
            LocalCacheHandler cacheHandler, ThanksTopPayData thanksTopPayData
    ) throws Exception {
        Gson afGSON = new Gson();
        Map[] mapResult = afGSON.fromJson(
                cacheHandler.getString(Jordan.CACHE_AF_KEY_ALL_PRODUCTS),
                new TypeToken<Map[]>() {
                }.getType()
        );
        ArrayList<Product> locaProducts = afGSON.fromJson(
                cacheHandler.getString(Jordan.CACHE_LC_KEY_ALL_PRODUCTS),
                new TypeToken<ArrayList<Product>>() {
                }.getType()
        );
        ArrayList<Purchase> purchases = afGSON.fromJson(
                cacheHandler.getString(Jordan.CACHE_KEY_DATA_AR_ALLPURCHASE),
                new TypeToken<ArrayList<Purchase>>() {
                }.getType()
        );

        JSONArray arrJas = new JSONArray(
                cacheHandler.getArrayListString(Jordan.CACHE_AF_KEY_JSONIDS)
        );
        String revenue = cacheHandler.getString(Jordan.CACHE_AF_KEY_REVENUE);
        int qty = cacheHandler.getInt(Jordan.CACHE_AF_KEY_QTY);
        String totalShipping = cacheHandler.getLong(Jordan.CACHE_LC_KEY_SHIPPINGRATE) + "";

        /**
         * GTM Block
         *
         */
        if (purchases != null) {
            if (!purchases.isEmpty()) {
                for (Purchase purchase : purchases) {
                    purchase.setTransactionID(thanksTopPayData.getParameter().getPaymentId());
                    PaymentTracking.eventTransactionGTM(purchase);
                }
            }
        }

        /**
         * AppsFlyer Block
         *
         */
        PaymentTracking.eventTransactionAF(
                thanksTopPayData.getParameter().getPaymentId(),
                revenue, arrJas, qty, mapResult
        );

          /*
            Branch.io block
         */
        BranchSdkUtils.sendCommerceEvent(locaProducts,revenue,totalShipping);

    }
}
