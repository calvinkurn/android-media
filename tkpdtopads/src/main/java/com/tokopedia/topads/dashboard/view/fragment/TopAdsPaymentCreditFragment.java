package com.tokopedia.topads.dashboard.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;

public class TopAdsPaymentCreditFragment extends TkpdBaseV4Fragment {

    TkpdWebView webView;
    ProgressBar progressBar;

    private DataCredit dataCredit;

    public static TopAdsPaymentCreditFragment createInstance() {
        TopAdsPaymentCreditFragment fragment = new TopAdsPaymentCreditFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCredit = getActivity().getIntent().getParcelableExtra(TopAdsConstant.EXTRA_CREDIT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        webView = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setIndeterminate(true);
        loadWeb();
    }

    private void loadWeb() {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.loadAuthUrl(URLGenerator.generateURLSessionLogin(Uri.encode(dataCredit.getProductUrl()), getActivity()));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (getActivity() != null && ((TkpdCoreRouter) getActivity().getApplication())
                        .isSupportedDelegateDeepLink(url)) {
                    ((TkpdCoreRouter) getActivity().getApplication())
                            .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}