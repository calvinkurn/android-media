package com.tokopedia.payment.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.payment.R;
import com.tokopedia.payment.activity.TopPayBaseActivity;
import com.tokopedia.payment.cart.listener.CartActivityListener;
import com.tokopedia.payment.cart.listener.ITopPayView;
import com.tokopedia.payment.cart.presenter.TopPayPresenter;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.webview.ScroogeWebView;

/**
 * Created by kris on 3/9/17. Tokopedia
 */

public class TopPayActivity extends TopPayBaseActivity implements ITopPayView {

    private static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";
    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static final String EXTRA_PARAMETER_TOP_PAY_RESULT = "EXTRA_PARAMETER_TOP_PAY_RESULT";
    public static final int PAYMENT_SUCCESS = 5;
    public static final int PAYMENT_CANCELLED = 6;
    public static final int PAYMENT_FAILED = 7;

    private TopPayPresenter presenter;
    private ScroogeWebView scroogeWebView;
    private ProgressBar progressBar;

    public static final int REQUEST_CODE = TopPayActivity.class.hashCode();

    public static Intent createInstance(Context context, PaymentPassData paymentPassData) {
        Intent intent = new Intent(context, TopPayActivity.class);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
        initView();
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        initVar();
        setViewListener();
        setActionVar();
    }

    private void setActionVar() {
        presenter.proccessUriPayment(presenter.getPaymentPassData());
    }

    private void setViewListener() {
        progressBar.setIndeterminate(true);
        scroogeWebView.setScroogeListener(cartActivityListener());
    }

    private void initVar() {

    }

    private void initView() {
        setContentView(R.layout.scrooge_payment);
        scroogeWebView = (ScroogeWebView) findViewById(R.id.scrooge_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void initialPresenter() {
        presenter = new TopPayPresenter(this);
    }

    private void setupURIPass(Uri data) {
    }

    private void setupBundlePass(Bundle extras) {
        presenter.setPaymentPassData((PaymentPassData) extras
                .getParcelable(EXTRA_PARAMETER_TOP_PAY_DATA));
        scroogeWebView.initiateScroogeData(presenter.getPaymentPassData());
    }

    @Override
    public void renderWebViewPostUrl(String url, byte[] postData) {
        scroogeWebView.postUrl(url, postData);
    }

    @Override
    public void showToastMessageWithForceCloseView(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_MESSAGE, message);
        setResult(PAYMENT_CANCELLED, intent);
        closeView();
    }

    @Override
    public void setPaymentId(String paymentId) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    private CartActivityListener cartActivityListener() {
        return new CartActivityListener() {
            @Override
            public void onBackKeyPressed(String stringId) {
                presenter.processVerifyPaymentIdByCancelTopPay(stringId);
                onBackPressed();
            }

            @Override
            public void processVerifyPaymentId(Bundle bundle) {
                processPaymentSuccess(bundle);
            }

            @Override
            public void processPaymentFailed(Bundle bundle) {
                processPaymentFailure(bundle);
            }

            @Override
            public void showMessageWithForceCloseView(String errorMessage) {
                showToastMessageWithForceCloseView(errorMessage);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scroogeWebView.showError(WebViewClient.ERROR_TIMEOUT);
                    }
                });
            }
        };
    }

    public void closeView() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt(ScroogeWebView.EXTRA_ACTION,
                ScroogeWebView.SERVICE_ACTION_GET_THANKS_TOP_PAY);
        bundle.putString(ScroogeWebView.EXTRA_PAYMENT_ID, presenter.getCurrentPaymentId());
        cancelPayment(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void cancelPayment(Bundle bundle) {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_RESULT, bundle);
        setResult(PAYMENT_CANCELLED, intent);
        finish();
    }

    private void processPaymentSuccess(Bundle bundle) {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_RESULT, bundle);
        setResult(PAYMENT_SUCCESS, intent);
        finish();
    }

    private void processPaymentFailure(Bundle bundle) {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_RESULT, bundle);
        setResult(PAYMENT_FAILED, intent);
        finish();
    }

    private void hideProgressLoading() {

    }

    private void showProgressLoading() {

    }
}
