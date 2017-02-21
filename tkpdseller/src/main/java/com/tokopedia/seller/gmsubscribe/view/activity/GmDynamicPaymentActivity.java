package com.tokopedia.seller.gmsubscribe.view.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ProgressBar;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.seller.R;
import com.tokopedia.seller.SellerModuleRouter;

import java.io.UnsupportedEncodingException;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmDynamicPaymentActivity extends BasePresenterActivity {

    public static final String PAYMENT_URL = "PAYMENT_URL";
    public static final String PARAMETER_URL = "PARAMETER_URL";
    public static final String PAYMENT_ID = "PAYMENT_ID";
    public static final String CALLBACK_URL = "CALLBACK_URL";
    private static final String TAG = "GMPayment";
    private static final String CONTAINS_ACCOUNT_URL = "accounts.tokopedia.com";
    private static final String CONTAINS_LOGIN_URL = "login.pl";
    private static final long FORCE_TIMEOUT = 60000L;
    private static final String CHARSET_UTF_8 = "UTF-8";


    WebView webView;
    ProgressBar progressBar;
    private String callbackUrl;
    private String paymentUrl;
    private String parameter;
    private int paymentId;

    public static Intent startPaymentWebview(Context context,
                                             String paymentUrl,
                                             String parameter,
                                             Integer paymentId,
                                             String callbackUrl) {
        Intent intent = new Intent(context, GmDynamicPaymentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PAYMENT_URL, paymentUrl);
        bundle.putString(PARAMETER_URL, parameter);
        bundle.putInt(PAYMENT_ID, paymentId);
        bundle.putString(CALLBACK_URL, callbackUrl);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected void setupURIPass(Uri uri) {

    }

    @Override
    protected void setupBundlePass(Bundle bundle) {
        paymentUrl = bundle.getString(PAYMENT_URL);
        parameter = bundle.getString(PARAMETER_URL);
        paymentId = bundle.getInt(PAYMENT_ID);
        callbackUrl = bundle.getString(CALLBACK_URL);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fragment_general_web_view;
    }

    @Override
    protected void initView() {
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setViewListener() {
        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new TopPayWebViewClient());
        webView.setWebChromeClient(new TopPayWebViewChromeClient());
        webView.setOnKeyListener(getWebViewOnKeyListener());
    }

    @NonNull
    private View.OnKeyListener getWebViewOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            onBackPressed();
                            return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        try {
            byte[] postData = parameter.getBytes(CHARSET_UTF_8);
            webView.postUrl(paymentUrl, postData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_GM_SUBSCRIBE_PAYMENT;
    }

    private void processRedirectUrlContainsLoginUrl() {

    }

    private void processRedirectUrlContainsAccountUrl(String url) {

    }

    private void processRedirectUrlContainsTopPayCallbackUrl(String url) {
        if (getApplication() instanceof SellerModuleRouter) {
            ((SellerModuleRouter) getApplication()).goToHome(this);
        }
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.invalidate();
            if (url.contains(callbackUrl)) {
                view.stopLoading();
                processRedirectUrlContainsTopPayCallbackUrl(url);
                return true;
            } else if (url.contains(CONTAINS_ACCOUNT_URL)) {
                view.stopLoading();
                processRedirectUrlContainsAccountUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            } else if (url.contains(CONTAINS_LOGIN_URL)) {
                view.stopLoading();
                processRedirectUrlContainsLoginUrl();
                return super.shouldOverrideUrlLoading(view, url);
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
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                showError(view, error.getErrorCode());
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showError(view, WebViewClient.ERROR_TIMEOUT);
                            }
                        });
                    }
                }
            }).start();
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        }

        private void showError(WebView view, int errorCode) {
            String message;
            switch (errorCode) {
                case WebViewClient.ERROR_TIMEOUT:
                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                    break;
                default:
                    message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                    break;
            }
            view.stopLoading();
        }
    }

    private class TopPayWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
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


}
