package com.tokopedia.tkpd.tkpdfeed.feedplus.view.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;

import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.util.TkpdWebViewClient;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity.BlogWebViewActivity;

import butterknife.BindView;

/**
 * @author by nisie on 5/19/17.
 */

public class BlogWebViewFragment extends BasePresenterFragment {

    @BindView(R2.id.webview)
    TkpdWebView webView;

    public static BlogWebViewFragment createInstance(Bundle bundle) {
        BlogWebViewFragment fragment = new BlogWebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        webView.loadAuthUrl(getArguments().getString(BlogWebViewActivity.PARAM_URL));
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_blog_webview;
    }

    @Override
    protected void initView(View view) {
        if (webView != null) {
            webView.clearCache(true);
        }
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(false);
        MethodChecker.setAllowMixedContent(webSettings);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private class MyWebClient extends TkpdWebViewClient {

        @Override
        protected boolean onOverrideUrl(Uri url) {
            return false;
        }
    }

    public BlogWebViewActivity.BackButtonListener getBackButtonListener() {
        return new BlogWebViewActivity.BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                }

            }

            @Override
            public boolean canGoBack() {
                return webView != null && webView.canGoBack();
            }
        };
    }
}
