package com.tokopedia.transaction.cart.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.receiver.CartBadgeNotificationReceiver;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.listener.ITopPayView;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.presenter.ITopPayPresenter;
import com.tokopedia.transaction.cart.presenter.TopPayPresenter;
import com.tokopedia.transaction.cart.receivers.TopPayBroadcastReceiver;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 12/8/16.
 *         Do not use!
 *         Please
 * @see com.tokopedia.payment.activity.TopPayActivity
 */
@Deprecated
public class TopPayActivity extends BasePresenterActivity<ITopPayPresenter> implements ITopPayView,
        TopPayBroadcastReceiver.ActionTopPayThanksListener {
    private static final String TAG = TopPayActivity.class.getSimpleName();
    public static final int REQUEST_CODE = TopPayActivity.class.hashCode();

    private static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";
    private static final String CONTAINS_ACCOUNT_URL = "accounts.tokopedia.com";
    private static final String CONTAINS_LOGIN_URL = "login.pl";
    private static final String PAYMENT_FAILED = "payment failed";
    private static final long FORCE_TIMEOUT = 60000L;
    public static final int RESULT_TOPPAY_CANCELED_OR_NOT_VERIFIED = TopPayActivity.class.hashCode();
    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";

    @BindView(R2.id.webview)
    WebView webView;
    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    private TkpdProgressDialog progressDialogNormal;
    private String paymentId;
    private TopPayParameterData topPayParameterData;
    private TopPayBroadcastReceiver topPayBroadcastReceiver;

    public static Intent createInstance(Context context, TopPayParameterData parameterData) {
        Intent intent = new Intent(context, TopPayActivity.class);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, parameterData);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        topPayParameterData = extras.getParcelable(EXTRA_PARAMETER_TOP_PAY_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopPayPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_pay;
    }

    @Override
    protected void initView() {
        progressDialogNormal = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
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

    @Override
    protected void initVar() {
        topPayBroadcastReceiver = new TopPayBroadcastReceiver(this);
        registerReceiver(topPayBroadcastReceiver, new IntentFilter(
                TopPayBroadcastReceiver.ACTION_GET_THANKS_TOP_PAY
        ));
    }

    @Override
    protected void setActionVar() {
        presenter.proccessUriPayment(topPayParameterData);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CART_SUMMARY_CHECKOUT;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void showProgressLoading() {
        progressDialogNormal.setCancelable(false);
        progressDialogNormal.showDialog();
    }

    @Override
    public void hideProgressLoading() {
        progressDialogNormal.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        View view = findViewById(android.R.id.content);
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void closeView() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(topPayBroadcastReceiver);
    }

    @Override
    public void onGetThanksTopPaySuccess(ThanksTopPayData data) {
        presenter.clearNotificationCart();
        try {
            presenter.processPaymentAnalytics(
                    new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA), data
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideProgressLoading();
        showToastMessage("Pembayaran berhasil");
        navigateToActivity(TransactionPurchaseRouter.createIntentTxSummary(this));
        CartBadgeNotificationReceiver.resetBadgeCart(this);
        closeView();
    }

    @Override
    public void onGetThanksTopPayFailed(String message) {
        hideProgressLoading();
        NetworkErrorHelper.createSnackbarWithAction(this, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        closeView();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void onGetThanksTopPayNotValid(String message) {
        hideProgressLoading();
        showToastMessageWithForceCloseView(message);
    }

    @Override
    public void onGetThanksTopPayNoConnection(String message) {
        hideProgressLoading();
        NetworkErrorHelper.showDialog(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.processVerifyPaymentId();
                    }
                });
    }

    @Override
    public void onGetThanksTopPayOngoing(String message) {
        showProgressLoading();
    }

    @Override
    public void onBackPressed() {
        presenter.processVerifyPaymentIdByCancelTopPay(
                paymentId != null ? paymentId : topPayParameterData.getParameter().getTransactionId()
        );
    }

    @Override
    public void renderWebViewPostUrl(String url, byte[] postData) {
        webView.postUrl(url, postData);
    }


    @Override
    public void showToastMessageWithForceCloseView(String message) {
//        View view = findViewById(android.R.id.content);
//        if (view != null) {
//            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setCallback(
//                    getCallbackToCloseView()).show();
//        } else {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//            closeView();
//        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_MESSAGE, message);
        setResult(RESULT_TOPPAY_CANCELED_OR_NOT_VERIFIED, intent);
        closeView();
    }

    @Override
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    ) {
        return null;
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, clazz).putExtras(bundle);
        this.startService(intent);
    }

    @Override
    public LocalCacheHandler getLocalCacheHandlerNotification() {
        return new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA);
    }

    @NonNull
    @Override
    public String getPaymentId() {
        return paymentId != null ? paymentId : topPayParameterData.getParameter().getTransactionId();
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

    @NonNull
    private Snackbar.Callback getCallbackToCloseView() {
        return new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                closeView();
            }
        };
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "REDIRECT URL = " + url);
            view.invalidate();
            if (url.contains(topPayParameterData.getCallbackUrlPath())) {
                view.stopLoading();
                presenter.processRedirectUrlContainsTopPayCallbackUrl(url);
                return true;
            } else if (url.contains(CONTAINS_ACCOUNT_URL)) {
                view.stopLoading();
                presenter.processRedirectUrlContainsAccountUrl(url);
                return true;
            } else if (url.contains(CONTAINS_LOGIN_URL)) {
                view.stopLoading();
                presenter.processRedirectUrlContainsLoginUrl();
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
            showToastMessageWithForceCloseView(message);
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
