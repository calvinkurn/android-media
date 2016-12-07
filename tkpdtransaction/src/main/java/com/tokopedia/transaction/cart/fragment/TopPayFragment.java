package com.tokopedia.transaction.cart.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
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

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.cart.listener.ICartActionFragment;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 11/29/16.
 */

public class TopPayFragment extends BasePresenterFragment implements View.OnKeyListener {
    private static final String TAG = TopPayFragment.class.getSimpleName();
    private static final String EXTRA_ARGS_DYNAMIC_PAYMENT_DATA = "EXTRA_ARGS_DYNAMIC_PAYMENT_DATA";
    private static final String MESSAGE_PAYMENT_SUCCESS = "Pembayaran Berhasil";
    private static final String MESSAGE_PAYMENT_CANCELED = "Proses pembayaran dibatalkan";
    private static final String MESSAGE_PAYMENT_FAILED = "Proses pembayaran gagal, coba kembali";
    private static final String KEY_QUERY_PAYMENT_ID = "id";
    private static final String KEY_QUERY_LD = "ld";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTAINS_ACCOUNT_URL = "accounts.tokopedia.com";
    private static final String CONTAINS_LOGIN_URL = "login.pl";
    private static final long FORCE_TIMEOUT = 60000L;

    @BindView(R2.id.webview)
    WebView webView;
    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    private TopPayParameterData topPayParameterData;
    private ICartActionFragment actionListener;
    private String paymentId;

    public static TopPayFragment newInstance(TopPayParameterData data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ARGS_DYNAMIC_PAYMENT_DATA, data);
        TopPayFragment fragment = new TopPayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ICartActionFragment) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.topPayParameterData = arguments.getParcelable(EXTRA_ARGS_DYNAMIC_PAYMENT_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_pay_tx_module;
    }

    @Override
    protected void initView(View view) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setViewListener() {
        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new TopPayWebViewClient());
        webView.setWebChromeClient(new TopPayWebViewChromeClient());
        webView.setOnKeyListener(this);
    }

    @Override
    protected void initialVar() {

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

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (paymentId != null && !paymentId.isEmpty()) {
                        actionListener.onTopPaySuccess(paymentId, MESSAGE_PAYMENT_SUCCESS);
                    } else {
                        actionListener.onTopPayCanceled(MESSAGE_PAYMENT_CANCELED);
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (paymentId != null && !paymentId.isEmpty()) {
                    actionListener.onTopPaySuccess(paymentId, MESSAGE_PAYMENT_SUCCESS);
                } else {
                    actionListener.onTopPayCanceled(MESSAGE_PAYMENT_CANCELED);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void testMethod() {
        Toast.makeText(getActivity(), "Holalalalalalalalala", Toast.LENGTH_SHORT).show();
        startActivity(TransactionPurchaseRouter.createIntentTxSummary(getActivity()));
        getActivity().finish();
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.invalidate();
            paymentId = Uri.parse(url).getQueryParameter(KEY_QUERY_PAYMENT_ID);
            if (url.contains(topPayParameterData.getCallbackUrlPath())) {
                Uri uri = Uri.parse(url);
                String id = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                paymentId = id;
                actionListener.onTopPaySuccess(id, MESSAGE_PAYMENT_SUCCESS);
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
                String id = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                paymentId = id;
                actionListener.onTopPaySuccess(id, MESSAGE_PAYMENT_SUCCESS);
                return true;
            } else if (url.contains(CONTAINS_LOGIN_URL)) {
                view.stopLoading();
                actionListener.onTopPayFailed(MESSAGE_PAYMENT_FAILED);
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
                        getActivity().runOnUiThread(new Runnable() {
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
            String message = "";
            switch (errorCode) {
                case WebViewClient.ERROR_TIMEOUT:
                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                    break;
                default:
                    message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                    break;
            }
            view.stopLoading();
            if (actionListener != null) actionListener.onTopPayFailed(message);
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

        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }
}
