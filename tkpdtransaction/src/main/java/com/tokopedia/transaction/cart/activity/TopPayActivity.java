package com.tokopedia.transaction.cart.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
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
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.listener.ITopPayView;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;
import com.tokopedia.transaction.cart.receivers.TopPayBroadcastReceiver;
import com.tokopedia.transaction.cart.services.TopPayIntentService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 12/8/16.
 */

public class TopPayActivity extends BasePresenterActivity implements ITopPayView,
        TopPayBroadcastReceiver.ActionTopPayThanksListener, View.OnKeyListener {
    private static final String TAG = TopPayActivity.class.getSimpleName();

    private static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";
    private static final String MESSAGE_PAYMENT_FAILED = "Proses pembayaran gagal, coba kembali";
    private static final String KEY_QUERY_PAYMENT_ID = "id";
    private static final String KEY_QUERY_LD = "ld";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTAINS_ACCOUNT_URL = "accounts.tokopedia.com";
    private static final String CONTAINS_LOGIN_URL = "login.pl";
    private static final long FORCE_TIMEOUT = 60000L;
    public static final int REQUEST_CODE = 1005;

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

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_top_pay;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new TopPayWebViewClient());
        webView.setWebChromeClient(new TopPayWebViewChromeClient());
        webView.setOnKeyListener(this);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {
        progressDialogNormal = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        topPayBroadcastReceiver = new TopPayBroadcastReceiver(this);
        registerReceiver(topPayBroadcastReceiver, new IntentFilter(
                TopPayBroadcastReceiver.ACTION_GET_THANKS_TOP_PAY
        ));
    }

    @Override
    protected void setActionVar() {
        try {
            webView.postUrl(topPayParameterData.getRedirectUrl(),
                    topPayParameterData.getQueryString().getBytes(CHARSET_UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CART_SUMMARY_CHECKOUT;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        super.onBackPressed();
                    }
                    return true;
            }
        }
        return false;
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
    public void onGetThanksTopPaySuccess(String message) {
        hideProgressLoading();
        showToastMessage(message);
        navigateToActivity(TransactionPurchaseRouter.createIntentTxSummary(this));
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
                });
    }

    @Override
    public void onGetThanksTopPayNoConnection(String message) {
        hideProgressLoading();
        NetworkErrorHelper.showDialog(this,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        actionTopPaySucess();
                    }
                });
    }

    @Override
    public void onGetThanksTopPayOngoing(String message) {
        showProgressLoading();
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.invalidate();
            if (url.contains(topPayParameterData.getCallbackUrlPath())) {
                Uri uri = Uri.parse(url);
                paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                actionTopPaySucess();
                return true;
            } else if (url.contains(CONTAINS_ACCOUNT_URL)) {
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
                paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                actionTopPaySucess();
                return true;
            } else if (url.contains(CONTAINS_LOGIN_URL)) {
                view.stopLoading();
                actionTopPayFailed(MESSAGE_PAYMENT_FAILED);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showError(view, error.getErrorCode());
            }
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
            actionTopPayFailed(message);
        }
    }

    private void actionTopPayFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void actionTopPaySucess() {
        if (paymentId == null) {
            onGetThanksTopPayFailed("Pembayaran gagal, Mohon coba kembali");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
                TopPayIntentService.class);
        intent.putExtra(TopPayIntentService.EXTRA_ACTION,
                TopPayIntentService.SERVICE_ACTION_GET_THANKS_TOP_PAY);
        intent.putExtra(TopPayIntentService.EXTRA_PAYMENT_ID, paymentId);
        startService(intent);
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

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
