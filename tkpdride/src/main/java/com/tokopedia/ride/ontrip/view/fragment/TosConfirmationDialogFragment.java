package com.tokopedia.ride.ontrip.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.ride.R;

/**
 * Created by alvarisi on 3/29/17.
 */

public class TosConfirmationDialogFragment extends DialogFragment {
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_ID = "EXTRA_ID";

    private ProgressBar progressBar;
    private TkpdWebView webviewRecharge;

    public static TosConfirmationDialogFragment newInstance(String url) {
        TosConfirmationDialogFragment fragment = new TosConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_tos_confirmation, container);
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    private class TosConfirmationWebClient extends WebViewClient {
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
        public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
            Uri uri = Uri.parse(urlString);
            if (uri.getScheme().equals("toko")){
                String id = uri.getQueryParameter("tos_confirmation_id");
                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_ID, id);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        intent
                );
                dismiss();
            }

            return false;
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webviewRecharge = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        String url = getArguments().getString(EXTRA_URL);
        progressBar.setIndeterminate(true);
        clearCache(webviewRecharge);
        webviewRecharge.loadAuthUrlWithFlags(url);
        webviewRecharge.setWebViewClient(new TosConfirmationWebClient());
        webviewRecharge.setWebChromeClient(new MyWebViewClient());
        WebSettings webSettings = webviewRecharge.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webviewRecharge.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webviewRecharge.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}
