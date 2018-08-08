package com.tokopedia.transaction.wallet;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.webview.fragment.BaseWebViewClient;
import com.tokopedia.transaction.R;

/**
 * Created by kris on 1/13/17. Tokopedia
 */

public class WalletActivity extends TActivity implements BaseWebViewClient.WebViewCallback, View.OnKeyListener {

    public static final String EXTRA_URL = "url";
    private String url;

    private TkpdWebView webView;
    private ProgressBar progressBar;


    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getIntent().getExtras().getString(EXTRA_URL);
        inflateView(R.layout.wallet_webview);
        initiateView();
        setWebView();
        showFragmentWebView();
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

    private void initiateView() {
        webView = (TkpdWebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        webView.setOnKeyListener(this);
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
            clearTokoCashData();
            finish();
        }
    }

    private void clearTokoCashData() {
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
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        clearTokoCashData();
        super.onBackPressed();
    }

    @Override
    public boolean onOverrideUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri.getPath().contains("thanks_wallet")) {
            clearTokoCashData();
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onWebTitlePageCompleted(String title) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
