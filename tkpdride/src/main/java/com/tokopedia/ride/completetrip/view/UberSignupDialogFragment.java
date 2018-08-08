package com.tokopedia.ride.completetrip.view;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
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

public class UberSignupDialogFragment extends DialogFragment {
    private static final String TAG = "UberSignupDialog";

    public static final String EXTRA_URL = "EXTRA_URL";
    private static final String REDIRECT_ACTION_BACK = "action_back";

    private ProgressBar progressBar;
    private TkpdWebView webview;

    public static UberSignupDialogFragment newInstance(String url) {
        UberSignupDialogFragment fragment = new UberSignupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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

    private class InterruptConfirmationWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            try {
                if (getActivity() != null) {
                    getActivity().setProgressBarIndeterminateVisibility(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
            Log.d(TAG, "shouldOverrideUrlLoading: " + urlString);
            if (urlString.endsWith(REDIRECT_ACTION_BACK)) {
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
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        String url = getArguments().getString(EXTRA_URL);

        progressBar.setIndeterminate(true);
        clearCache(webview);
        webview.loadUrl(url);
        webview.setWebViewClient(new InterruptConfirmationWebClient());
        webview.setWebChromeClient(new MyWebViewClient());
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
