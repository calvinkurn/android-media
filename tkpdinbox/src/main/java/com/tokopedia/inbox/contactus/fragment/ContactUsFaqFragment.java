package com.tokopedia.inbox.contactus.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.container.GTMContainer;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity.BackButtonListener;
import com.tokopedia.core.var.TkpdUrl;

import butterknife.Bind;

import static com.tokopedia.core.analytics.container.GTMContainer.getContainer;
import static com.tokopedia.inbox.contactus.ContactUsConstant.PARAM_URL;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsFaqFragment extends BasePresenterFragment {

    private static final String GTM_CONTACTUS_URL = "url_contactus";

    @Bind(R2.id.scroll_view)
    ScrollView mainView;

    @Bind(R2.id.webview)
    WebView webView;

    @Bind(R2.id.progressbar)
    ProgressBar progressBar;

    ContactUsFaqListener listener;
    String url;

    public interface ContactUsFaqListener {
        void onGoToCreateTicket();
    }

    public static ContactUsFaqFragment createInstance(Bundle extras) {
        ContactUsFaqFragment fragment = new ContactUsFaqFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onFirstTimeLaunched() {
        String url;
        if (getArguments().getString(PARAM_URL, "").equals("")) {
            if (!GTMContainer.getContainer().getString(GTM_CONTACTUS_URL).equals(""))
                url = GTMContainer.getContainer().getString(GTM_CONTACTUS_URL) + "&app_version=" + GlobalConfig.VERSION_CODE;
            else
                url = TkpdUrl.CONTACT_US_FAQ + "&app_version=" + GlobalConfig.VERSION_CODE;
        } else
            url = getArguments().getString(PARAM_URL);

        webView.loadUrl(url);


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
        return R.layout.fragment_contact_us_faq;
    }

    @Override
    protected void initView(View view) {
        if (webView != null) {
            webView.clearCache(true);
        }
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new MyWebViewClient());
        progressBar.setIndeterminate(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        listener = (ContactUsActivity) getActivity();
    }

    @Override
    protected void setActionVar() {

    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    webView.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                progressBar.setIndeterminate(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            try {
                handler.proceed();
            } catch (Exception e) {
                super.onReceivedSslError(view, handler, error);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mainView != null)
                mainView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainView.smoothScrollTo(0, 0);
                    }
                }, 300);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (Uri.parse(url).getLastPathSegment().equals("contact-us-android")) {
                    listener.onGoToCreateTicket();
                    return true;
                } else {
                    return false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressBar != null)
            progressBar.setIndeterminate(false);
    }

    public BackButtonListener getBackButtonListener() {
        return new BackButtonListener() {
            @Override
            public void onBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                }

            }

            @Override
            public boolean canGoBack() {
                return webView.canGoBack();
            }
        };
    }
}
