package com.tokopedia.ride.history.view;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.webview.fragment.BaseWebViewClient;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

    public class RideHistoryNeedHelpActivity extends BaseActivity implements BaseWebViewClient.WebViewCallback {
    private static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    private RideHistoryViewModel rideHistory;

    @BindView(R2.id.webview)
    TkpdWebView WebViewGeneral;
    @BindView(R2.id.progressbar)
    ProgressBar progressBar;
    private Unbinder unbinder;

    public static Intent getCallingIntent(Activity activity, RideHistoryViewModel rideHistory) {
        Intent intent = new Intent(activity, RideHistoryNeedHelpActivity.class);
        intent.putExtra(EXTRA_REQUEST_ID, rideHistory);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_need_help);
        unbinder = ButterKnife.bind(this);
        rideHistory = getIntent().getParcelableExtra(EXTRA_REQUEST_ID);
        setupToolbar();
        init();
    }

    private void setupToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.help_toolbar_title));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
    }

    private void init() {
        CookieManager.getInstance().setAcceptCookie(true);
        progressBar.setIndeterminate(true);
        WebViewGeneral.loadAuthUrl(rideHistory.getHelpUrl());
        WebViewGeneral.getSettings().setJavaScriptEnabled(true);
        WebViewGeneral.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewGeneral.getSettings().setDomStorageEnabled(true);
        WebViewGeneral.setWebViewClient(new BaseWebViewClient(this));
        WebViewGeneral.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //  progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccessResult(String successResult) {

    }

    @Override
    public void onProgressResult(String progressResult) {

    }

    @Override
    public void onErrorResult(SslError errorResult) {

    }

    @Override
    public void onBackPressed() {
        RideGATracking.eventBackPress(getScreenName());
        if (WebViewGeneral.canGoBack()) {
            WebViewGeneral.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOverrideUrl(String url) {
        if (url.endsWith("action_back") || url.endsWith("tokopedia://close")) {
            finish();
        }
        return false;
    }

    @Override
    public void onWebTitlePageCompleted(String title) {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_RIDE_HISTORY_NEED_HELP;
    }
}
