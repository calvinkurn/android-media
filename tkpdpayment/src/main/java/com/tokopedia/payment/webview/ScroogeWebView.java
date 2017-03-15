package com.tokopedia.payment.webview;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.payment.cart.listener.CartActivityListener;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ScroogeWebView extends WebView {
    private static final String TAG = ScroogeWebView.class.getSimpleName();

    private static final String LOGIN_URL = "login.pl";
    private static final String ACCOUNTS_URL = "accounts.tokopedia.com";
    public static final String KEY_QUERY_PAYMENT_ID = "id";
    public static final int SERVICE_ACTION_GET_THANKS_TOP_PAY = 2;
    public static final String EXTRA_ACTION = "EXTRA_ACTION";
    public static final String EXTRA_PAYMENT_ID = "EXTRA_PAYMENT_ID";
    public static final String KEY_QUERY_LD = "ld";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final long FORCE_TIMEOUT = 60000L;

    private PaymentPassData paymentPassData;
    private CartActivityListener cartActivityListener;
    private String paymentId;

    public ScroogeWebView(Context context) {
        super(context);
        initWebViewSettings();
    }

    public ScroogeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSettings();
    }

    public ScroogeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebViewSettings();
    }

    public void initiateScroogeData(PaymentPassData paymentPassData) {
        this.paymentPassData = paymentPassData;
    }

    public void setScroogeListener(CartActivityListener listener) {
        cartActivityListener = listener;
    }

    public String getPaymentId() {
        return paymentId;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(true);
        getSettings().setAppCacheEnabled(true);
        setWebViewClient(new TopPayWebViewClient());
        setWebChromeClient(new TopPayWebViewChromeClient());
        setOnKeyListener(getWebViewOnKeyListener());
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.invalidate();
            if (url.contains(paymentPassData.getCallbackSuccessUrl())) {
                view.stopLoading();
                processRedirectUrlContainsTopPayCallbackUrl(url);
                return true;
            } else if (url.contains(ACCOUNTS_URL)) {
                view.stopLoading();
                processRedirectUrlContaintsAccountsUrl(url);
                return true;
            } else if (url.contains(LOGIN_URL)) {
                view.stopLoading();
                cartActivityListener
                        .showMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TOPPAY);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
            cartActivityListener.hideProgressBar();
            //if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            cartActivityListener.hideProgressBar();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                showError(error.getErrorCode());
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(FORCE_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (timeout) {
                        cartActivityListener.showTimeoutErrorOnUiThread();
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                showError(view, WebViewClient.ERROR_TIMEOUT);
                            }
                        });*/
                    }
                }
            }).start();
            cartActivityListener.showProgressBar();
        }
    }

    public void showError(int errorCode) {
        String message;
        switch (errorCode) {
            case WebViewClient.ERROR_TIMEOUT:
                message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }
        stopLoading();
        cartActivityListener.showMessageWithForceCloseView(message);
    }

    private void processRedirectUrlContaintsAccountsUrl(String url) {
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
        this.paymentId = paymentId;
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ACTION, SERVICE_ACTION_GET_THANKS_TOP_PAY);
        bundle.putString(EXTRA_PAYMENT_ID, paymentId);
        cartActivityListener.processVerifyPaymentId(bundle);
    }

    private void processRedirectUrlContainsTopPayCallbackUrl(String url) {
        Uri uri = Uri.parse(url);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        this.paymentId = paymentId;
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ACTION, SERVICE_ACTION_GET_THANKS_TOP_PAY);
        bundle.putString(EXTRA_PAYMENT_ID, paymentId);
        cartActivityListener.processVerifyPaymentId(bundle);
    }

    private class TopPayWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                cartActivityListener.hideProgressBar();
                //if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @SuppressWarnings("deprecation")
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    private View.OnKeyListener getWebViewOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            cartActivityListener.onBackKeyPressed(paymentId);
                            return true;
                    }
                }
                return false;
            }
        };
    }
}
