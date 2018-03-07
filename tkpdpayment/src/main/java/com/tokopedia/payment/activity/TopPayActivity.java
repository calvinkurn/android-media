package com.tokopedia.payment.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.payment.R;
import com.tokopedia.payment.listener.ITopPayView;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.presenter.TopPayPresenter;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.payment.utils.Constant;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Created by kris on 3/9/17. Tokopedia
 */

public class TopPayActivity extends Activity implements ITopPayView {
    private static final String TAG = TopPayActivity.class.getSimpleName();

    public static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";

    private static final String ACCOUNTS_URL = "accounts.tokopedia.com";
    public static final String KEY_QUERY_PAYMENT_ID = "id";
    public static final String KEY_QUERY_LD = "ld";
    public static final String CHARSET_UTF_8 = "UTF-8";
    private static final String LOGIN_URL = "login.pl";
    private static final String[] THANK_PAGE_URL_LIST = new String[]{"thanks", "thank"};

    public static final int PAYMENT_SUCCESS = 5;
    public static final int PAYMENT_CANCELLED = 6;
    public static final int PAYMENT_FAILED = 7;
    public static final long FORCE_TIMEOUT = 90000L;

    private TopPayPresenter presenter;
    private WebView scroogeWebView;
    private ProgressBar progressBar;
    private PaymentPassData paymentPassData;
    private IPaymentModuleRouter paymentModuleRouter;

    private View btnBack;
    private View btnClose;
    private TextView tvTitle;

    public static final int REQUEST_CODE = 980;

    public static Intent createInstance(Context context, PaymentPassData paymentPassData) {
        Intent intent = new Intent(context, TopPayActivity.class);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        return intent;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getResources().getColor(
                        R.color.tkpd_status_green_payment_module, null
                ));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(
                            R.color.tkpd_status_green_payment_module
                    ));
                }
            }
        }
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        if (getApplication() instanceof IPaymentModuleRouter) {
            paymentModuleRouter = (IPaymentModuleRouter) getApplication();
        }
        initialPresenter();
        initView();
        initVar();
        setViewListener();
        setActionVar();
    }

    private void setActionVar() {
        presenter.proccessUriPayment();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setViewListener() {
        progressBar.setIndeterminate(true);
        scroogeWebView.getSettings().setJavaScriptEnabled(true);
        scroogeWebView.getSettings().setDomStorageEnabled(true);
        scroogeWebView.getSettings().setBuiltInZoomControls(false);
        scroogeWebView.getSettings().setDisplayZoomControls(true);
        scroogeWebView.getSettings().setAppCacheEnabled(true);
        scroogeWebView.setWebViewClient(new TopPayWebViewClient());
        scroogeWebView.setWebChromeClient(new TopPayWebViewChromeClient());
        scroogeWebView.setOnKeyListener(getWebViewOnKeyListener());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentModuleRouter != null && paymentModuleRouter.getBaseUrlDomainPayment() != null
                        && scroogeWebView.getUrl() != null
                        && scroogeWebView.getUrl().contains(paymentModuleRouter.getBaseUrlDomainPayment()))
                    scroogeWebView.loadUrl("javascript:handlePopAndroid();");
                else
                    onBackPressed();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackPaymentCanceled();
            }
        });
    }

    private void initVar() {

    }

    private void initView() {
        setContentView(R.layout.activity_top_pay_payment_module);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnBack = findViewById(R.id.btn_back);
        btnClose = findViewById(R.id.btn_close);
        scroogeWebView = (WebView) findViewById(R.id.scrooge_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void initialPresenter() {
        presenter = new TopPayPresenter(this);
    }

    private void setupURIPass(Uri data) {
    }

    private void setupBundlePass(Bundle extras) {
        this.paymentPassData = extras.getParcelable(EXTRA_PARAMETER_TOP_PAY_DATA);
    }

    @Override
    public void renderWebViewPostUrl(String url, byte[] postData) {
        scroogeWebView.postUrl(url, postData);
    }

    @Override
    public void showToastMessageWithForceCloseView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        callbackPaymentCanceled();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public PaymentPassData getPaymentPassData() {
        return paymentPassData;
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void hideProgressBar() {
        hideProgressLoading();
    }

    @Override
    public void showProgressBar() {
        showProgressLoading();
    }

    @Override
    public void showTimeoutErrorOnUiThread() {

    }

    @Override
    public void setWebPageTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void backStackAction() {
        onBackPressed();
    }

    public void closeView() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (isEndThanksPage()) {
            callbackPaymentSucceed();
        } else {
            callbackPaymentCanceled();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void callbackPaymentCanceled() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_CANCELLED, intent);
        finish();
    }

    public void callbackPaymentSucceed() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_SUCCESS, intent);
        finish();
    }

    public void callbackPaymentFailed() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_FAILED, intent);
        finish();
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //Log.d(TAG, "redirect url = " + url);

            /*
              HANYA SEMENTARA HARCODE, NANTI PAKAI APPLINK
             */
            if (!url.isEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_LIVE
                    + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY)
                    || url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_STAGING
                    + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY))) {
                String deepLinkUrl = Constant.TempRedirectPayment.APP_LINK_SCHEME_WEB_VIEW
                        + "?url=" + URLEncoder.encode(url);
                paymentModuleRouter.actionAppLinkPaymentModule(
                        TopPayActivity.this, deepLinkUrl
                );
                return true;
            } else {
                if (!url.isEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_LIVE) ||
                        url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_STAGING))) {
                    paymentModuleRouter.actionAppLinkPaymentModule(
                            TopPayActivity.this, Constant.TempRedirectPayment.APP_LINK_SCHEME_HOME
                    );
                    return true;
                }
            }

            if (!TextUtils.isEmpty(paymentPassData.getCallbackSuccessUrl()) &&
                    url.contains(paymentPassData.getCallbackSuccessUrl())) {
                view.stopLoading();
                callbackPaymentSucceed();
                return true;
            } else if (!TextUtils.isEmpty(paymentPassData.getCallbackFailedUrl()) &&
                    url.contains(paymentPassData.getCallbackFailedUrl())) {
                view.stopLoading();
                callbackPaymentFailed();
                return true;
            } else if (url.contains(ACCOUNTS_URL)) {
                view.stopLoading();
                processRedirectUrlContaintsAccountsUrl(url);
                return true;
            } else if (url.contains(LOGIN_URL)) {
                view.stopLoading();
                showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TOPPAY);
                return true;
            } else {
                if (paymentModuleRouter != null
                        && paymentModuleRouter.getSchemeAppLinkCancelPayment() != null
                        && paymentModuleRouter.getSchemeAppLinkCancelPayment().equalsIgnoreCase(url)) {
                    if (isEndThanksPage()) callbackPaymentSucceed();
                    else callbackPaymentCanceled();
                    return true;
                } else if (paymentModuleRouter != null
                        && paymentModuleRouter.isSupportedDelegateDeepLink(url)
                        && paymentModuleRouter.getIntentDeepLinkHandlerActivity() != null) {
                    Intent intent = paymentModuleRouter.getIntentDeepLinkHandlerActivity();
                    intent.setData(Uri.parse(url));
                    navigateToActivity(intent);
                    return true;
                } else if (paymentModuleRouter != null) {
                    String urlFinal = paymentModuleRouter.getGeneratedOverrideRedirectUrlPayment(url);
                    if (urlFinal == null)
                        return super.shouldOverrideUrlLoading(view, url);
                    view.loadUrl(
                            urlFinal,
                            paymentModuleRouter.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal)
                    );
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
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
            super.onReceivedError(view, request, error);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }


        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            //  Log.d(TAG, "start url = " + url);
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
            showToastMessageWithForceCloseView(message);
        }
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
            callbackPaymentSucceed();
        else
            callbackPaymentFailed();
    }

    private class TopPayWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                hideProgressBar();
            }
            super.onProgressChanged(view, newProgress);
        }

        @SuppressWarnings("deprecation")
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            //        Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            //        Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }

    public boolean isEndThanksPage() {
        if (scroogeWebView.getUrl() == null || scroogeWebView.getUrl().isEmpty()) return false;
        for (String thanksUrl : THANK_PAGE_URL_LIST) {
            if (scroogeWebView.getUrl().contains(thanksUrl)) {
                return true;
            }
        }
        return false;
    }

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

    private void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
