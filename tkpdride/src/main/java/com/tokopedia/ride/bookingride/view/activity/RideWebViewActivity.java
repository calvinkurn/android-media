package com.tokopedia.ride.bookingride.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.webview.fragment.BaseWebViewClient;
import com.tokopedia.ride.R;

public class RideWebViewActivity extends BasePresenterActivity implements BaseWebViewClient.WebViewCallback, View.OnKeyListener {
    private static final String EXTRA_TOKO_CASH_URL = "EXTRA_TOKO_CASH_URL";
    private String url;

    private TkpdWebView webView;
    private ProgressBar progressBar;

    public static Intent getCallingIntent(Activity activity, String url) {
        Intent intent = new Intent(activity, RideWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TOKO_CASH_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new BaseWebViewClient(this));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //  progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void showFragmentWebView() {
        webView.loadAuthUrl(url);
    }

    @Override
    public void onSuccessResult(String successResult) {
    }

    @Override
    public void onProgressResult(String progressResult) {
        Uri uri = Uri.parse(progressResult);
        if (uri.getPath().contains("thanks_wallet")) {
            deleteTokoCashCache();
            finish();
        }
    }

    private void deleteTokoCashCache() {
        GlobalCacheManager cacheManager = new GlobalCacheManager();
        cacheManager.delete(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
    }

    @Override
    public void onErrorResult(SslError errorResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        url = extras.getString(EXTRA_TOKO_CASH_URL);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_toko_cash_web_view;
    }

    @Override
    protected void initView() {
        webView = (TkpdWebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        webView.setOnKeyListener(this);
    }

    @Override
    protected void setViewListener() {
        setWebView();
        showFragmentWebView();
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        deleteTokoCashCache();
        super.onBackPressed();
    }

    @Override
    public boolean onOverrideUrl(String url) {
        return false;
    }

    @Override
    public void onWebTitlePageCompleted(String title) {

    }
}
