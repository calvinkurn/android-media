package com.tokopedia.loyalty.view.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.TkpdWebView;

/**
 * @author okasurya on 1/29/18.
 */

public class TokoPointWebViewFragment extends Fragment {
    private static final String SEAMLESS = "seamless";
    public static final int PROGRESS_COMPLETED = 100;
    private ProgressBar progressBar;
    private TkpdWebView webview;
    private static final String EXTRA_URL = "url";

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == PROGRESS_COMPLETED) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    getActivity().setProgressBarIndeterminateVisibility(false);
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
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (getActivity() != null && getActivity().getApplication() != null) {
                if (getActivity().getApplication() instanceof IDigitalModuleRouter && (((IDigitalModuleRouter) getActivity().getApplication())
                        .isSupportedDelegateDeepLink(url))) {
                    ((IDigitalModuleRouter) getActivity().getApplication())
                            .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                    return true;
                } else if (Uri.parse(url).getScheme().equalsIgnoreCase(Constants.APPLINK_CUSTOMER_SCHEME)) {
                    if (getActivity().getApplication() instanceof TkpdCoreRouter &&
                            (((TkpdCoreRouter) getActivity().getApplication()).getApplinkUnsupported(getActivity()) != null)) {

                        ((TkpdCoreRouter) getActivity().getApplication())
                                .getApplinkUnsupported(getActivity())
                                .showAndCheckApplinkUnsupported();
                        return true;
                    }
                }
            }

            return false;
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    public TokoPointWebViewFragment() {
    }

    public static TokoPointWebViewFragment createInstance(String url) {
        TokoPointWebViewFragment fragment = new TokoPointWebViewFragment();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(url)) {
            args.putString(EXTRA_URL, url);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.core.R.layout.fragment_fragment_general_web_view, container, false);
        String url = getArguments().getString(EXTRA_URL, TkpdBaseURL.MOBILE_DOMAIN);
        webview = (TkpdWebView) view.findViewById(com.tokopedia.core.R.id.webview);
        progressBar = (ProgressBar) view.findViewById(com.tokopedia.core.R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        if (!url.contains(SEAMLESS))
            webview.loadAuthUrl(URLGenerator.generateURLSessionLogin(url, getActivity()));
        else {
            webview.loadAuthUrl(url);
        }
        webview.setWebViewClient(new TokoPointWebViewFragment.MyWebClient());
        webview.setWebChromeClient(new TokoPointWebViewFragment.MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            webview.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        }
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
        return view;
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    public WebView getWebview() {
        return webview;
    }

    public void setWebview(TkpdWebView webview) {
        this.webview = webview;
    }

    private void optimizeWebView() {
        webview.setLayerType(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                        View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_SOFTWARE,
                null
        );
    }
}
