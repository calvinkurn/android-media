package com.tokopedia.seller.topads.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TopAdsPaymentCreditFragment extends TkpdFragment {

    @Bind(R2.id.webview)
    WebView webView;

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
        View view = inflater.inflate(R.layout.fragment_top_ads_payment_credit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        loadWeb();
    }

    private void loadWeb() {
        webView.loadUrl(URLGenerator.generateURLSessionLogin(Uri.encode(dataCredit.getProductUrl()), getActivity()));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}