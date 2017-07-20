package com.tokopedia.payment.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.payment.listener.ITopPayView;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.net.URLDecoder;

public class ScroogeWebView extends WebView {
    private static final String TAG = ScroogeWebView.class.getSimpleName();

    private static final String LOGIN_URL = "login.pl";
    private static final String ACCOUNTS_URL = "accounts.tokopedia.com";
    public static final String KEY_QUERY_PAYMENT_ID = "id";
    public static final String KEY_QUERY_LD = "ld";
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final long FORCE_TIMEOUT = 90000L;

    private static final String[] THANK_PAGE_URL_LIST = new String[]{"thanks", "thank"};

    private ITopPayView topPayView;

    public ScroogeWebView(Context context) {
        super(context);
    }

    public ScroogeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScroogeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setupAllSettings(ITopPayView view) {
        topPayView = view;
        initWebViewSettings();
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

    public boolean isEndThanksPage() {
        if (getUrl() == null || getUrl().isEmpty()) return false;
        for (String thanksUrl : THANK_PAGE_URL_LIST) {
            if (getUrl().contains(thanksUrl)) {
                return true;
            }
        }
        return false;
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;
        private final PaymentPassData paymentPassData;

        TopPayWebViewClient() {
            paymentPassData = topPayView.getPaymentPassData();
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "URL redirect " + url);
            if (url.contains(paymentPassData.getCallbackSuccessUrl())) {
                view.stopLoading();
                processRedirectUrlContainsSuccessCallbackUrl(url);
                return true;
            } else if (url.contains(paymentPassData.getCallbackFailedUrl())) {
                view.stopLoading();
                processRedirectUrlContainsFailedCallbackUrl(url);
                return true;
            } else if (url.contains(ACCOUNTS_URL)) {
                view.stopLoading();
                processRedirectUrlContaintsAccountsUrl(url);
                return true;
            } else if (url.contains(LOGIN_URL)) {
                view.stopLoading();
                topPayView.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TOPPAY);
                return true;
            } else {
                IPaymentModuleRouter paymentModuleRouter = topPayView.getPaymentModuleRouter();
                if (paymentModuleRouter == null) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else if (paymentModuleRouter.isSupportedDelegateDeepLink(url)) {
                    String appLinkScheme = paymentModuleRouter.getSchemeAppLinkCartPayment();
                    Intent intentCart = paymentModuleRouter.getIntentDeepLinkHandlerActivity();
                    if (appLinkScheme != null && appLinkScheme.equalsIgnoreCase(url)
                            && intentCart != null) {
                        intentCart.setData(Uri.parse(url));
                        topPayView.navigateToCart(intentCart);
                        return true;
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
            topPayView.hideProgressBar();
            topPayView.setWebPageTitle(view.getTitle());
            view.stopLoading();
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            topPayView.hideProgressBar();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "onReceivedError : " + errorCode + " " + description);
            showError(errorCode, description);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "URL initial " + url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(FORCE_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (timeout) {
                        topPayView.showTimeoutErrorOnUiThread();
                    }
                }
            }).start();
            topPayView.showProgressBar();
        }
    }

    public void showError(int errorCode, String description) {
        Log.e(TAG, "ERROR WEBVIEW : " + errorCode + " => " + description);
        switch (errorCode) {
            case WebViewClient.ERROR_TIMEOUT:
                description = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                break;
            default:
                if (description == null)
                    description = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                break;
        }
        topPayView.hideProgressBar();
        topPayView.showToastMessage(description);
    }

    private void processRedirectUrlContaintsAccountsUrl(String url) {
        Uri uriMain = Uri.parse(url);
        String ld = uriMain.getQueryParameter(KEY_QUERY_LD);
        String urlThanks;
        try {
            urlThanks = URLDecoder.decode(ld, CHARSET_UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            urlThanks = "";
        }
        Uri uri = Uri.parse(urlThanks);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        if (paymentId != null)
            topPayView.callbackPaymentSucceed();
        else
            topPayView.callbackPaymentFailed();
    }

    private void processRedirectUrlContainsSuccessCallbackUrl(String url) {
        topPayView.callbackPaymentSucceed();
    }

    private void processRedirectUrlContainsFailedCallbackUrl(String url) {
        topPayView.callbackPaymentFailed();
    }

    private class TopPayWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                topPayView.hideProgressBar();
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
                            topPayView.backStackAction();
                            return true;
                    }
                }
                return false;
            }
        };
    }

}
