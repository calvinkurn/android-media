package com.tokopedia.ride.scrooge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.ride.R;

public class ScroogeActivity extends AppCompatActivity {
    //callbacks URL's
    private static String ADD_CC_SUCESS_CALLBACK = "tokopedia://action_add_cc_success";
    private static String ADD_CC_FAIL_CALLBACK = "tokopedia://action_add_cc_fail";
    private static String DELETE_CC_SUCESS_CALLBACK = "tokopedia://action_delete_cc_success";
    private static String DELETE_CC_FAIL_CALLBACK = "tokopedia://action_delete_cc_fail";
    private static String SUCCESS_CALLBACK = "tokopedia://orderId";

    private static final String EXTRA_KEY_POST_PARAMS = "EXTRA_KEY_POST_PARAMS";
    private static final String EXTRA_KEY_URL = "URL";
    private static final String EXTRA_IS_POST_REQUEST = "EXTRA_IS_POST_REQUEST";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    private WebView mWebView;
    private ProgressBar mProgress;
    private Toolbar mToolbar;
    private String mPostParams;
    private String mURl;

    private int requestCode;
    private boolean isPostRequest;
    private String title;

    public static Intent getCallingIntent(Context context, String url, boolean isPostRequest, String postParams, String title) {
        Intent intent = new Intent(context, ScroogeActivity.class);
        intent.putExtra(EXTRA_KEY_POST_PARAMS, postParams);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_IS_POST_REQUEST, isPostRequest);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostParams = getIntent().getStringExtra(EXTRA_KEY_POST_PARAMS);
        mURl = getIntent().getStringExtra(EXTRA_KEY_URL);
        isPostRequest = getIntent().getBooleanExtra(EXTRA_IS_POST_REQUEST, false);
        title = getIntent().getStringExtra(EXTRA_TITLE);

        setContentView(R.layout.activity_scrooge_web_view);

        initUI();

        if (mPostParams == null) mPostParams = "";
        this.mWebView.postUrl(mURl, mPostParams.getBytes());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initUI() {
        mWebView = (WebView) findViewById(R.id.webview);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        //setup toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }

        mProgress.setIndeterminate(true);

        setupWebView(this.mWebView);
    }

    /**
     * Set up web view and register webview client
     *
     * @param webview
     */
    private void setupWebView(WebView webview) {
        if (webview == null) return;

        webview.setWebViewClient(new WebViewClient() {
            public synchronized void onPageStarted(WebView inView, String inUrl, Bitmap inFavicon) {
                super.onPageStarted(inView, inUrl, inFavicon);
                CommonUtils.dumper("ScroogeActivity :: onPageStarted url " + inUrl);

                try {
                    setProgressBarIndeterminateVisibility(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgress.setVisibility(View.VISIBLE);
            }

            public synchronized void onPageFinished(WebView inView, String inUrl) {
                super.onPageFinished(inView, inUrl);
                CommonUtils.dumper("ScroogeActivity :: onPageFinished url " + inUrl);
            }

            public synchronized void onReceivedError(WebView inView, int iniErrorCode, String inDescription, String inFailingUrl) {
                super.onReceivedError(inView, iniErrorCode, inDescription, inFailingUrl);
                CommonUtils.dumper("ScroogeActivity :: Error occured while loading url " + inFailingUrl);
                Intent responseIntent = new Intent();
                responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, inDescription);
                setResult(ScroogePGUtil.RESULT_CODE_RECIEVED_ERROR, responseIntent);
                finish();
            }

            public synchronized void onReceivedSslError(WebView inView, SslErrorHandler inHandler, SslError inError) {
                CommonUtils.dumper("ScroogeActivity :: SSL Error occured " + inError.toString());
                CommonUtils.dumper("ScroogeActivity :: SSL Handler is " + inHandler);
                if (inHandler != null) {
                    inHandler.proceed();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent responseIntent = new Intent();

                boolean returnVal = true;
                if (url.equalsIgnoreCase(ADD_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_SUCCESS, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(ADD_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Fail");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_FAIL, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(DELETE_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(DELETE_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "FAIL");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_SUCCESS, responseIntent);
                    finish();
                } else if (url.startsWith(SUCCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_SUCCESS, responseIntent);
                    finish();
                } else {
                    super.shouldOverrideUrlLoading(view, url);
                    returnVal = true;
                }

                return returnVal;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                try {
                    if (newProgress == 100) {
                        view.setVisibility(View.VISIBLE);
                        mProgress.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
    }
}
