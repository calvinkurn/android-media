package com.tokopedia.payment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.payment.R;
import com.tokopedia.payment.listener.ITopPayView;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.presenter.TopPayPresenter;
import com.tokopedia.payment.webview.ScroogeWebView;

/**
 * Created by kris on 3/9/17. Tokopedia
 */

public class TopPayActivity extends Activity implements ITopPayView {

    private static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";
    public static final int PAYMENT_SUCCESS = 5;
    public static final int PAYMENT_CANCELLED = 6;
    public static final int PAYMENT_FAILED = 7;

    private TopPayPresenter presenter;
    private ScroogeWebView scroogeWebView;
    private ProgressBar progressBar;
    private PaymentPassData paymentPassData;

    private View btnBack;
    private View btnClose;
    private TextView tvTitle;

    public static final int REQUEST_CODE = TopPayActivity.class.hashCode();

    public static Intent createInstance(Context context, PaymentPassData paymentPassData) {
        Intent intent = new Intent(context, TopPayActivity.class);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        return intent;
    }

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
        initialPresenter();
        initView();
        initVar();
        setViewListener();
        setActionVar();
    }

    private void setActionVar() {
        presenter.proccessUriPayment();
    }

    private void setViewListener() {
        progressBar.setIndeterminate(true);
        scroogeWebView.setupAllSettings(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        setContentView(R.layout.scrooge_payment);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnBack = findViewById(R.id.btn_back);
        btnClose = findViewById(R.id.btn_close);
        scroogeWebView = (ScroogeWebView) findViewById(R.id.scrooge_webview);
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
                scroogeWebView.showError(WebViewClient.ERROR_TIMEOUT, null);
            }
        });
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

        if (scroogeWebView.getPaymentId() != null || scroogeWebView.isEndThanksPage()) {
            callbackPaymentSucceed();
        } else {
            callbackPaymentCanceled();
        }

//        if (scroogeWebView.canGoBack()) {
//            scroogeWebView.goBack();
//        } else {
//            if (scroogeWebView.getPaymentId() != null || scroogeWebView.isEndThanksPage()) {
//                callbackPaymentSucceed();
//            } else {
//                callbackPaymentCanceled();
//            }
//        }
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

    private void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
