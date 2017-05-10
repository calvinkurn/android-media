package com.tokopedia.ride.ontrip.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.ride.R;

/**
 * Created by alvarisi on 3/29/17.
 */

public class InterruptConfirmationDialogFragment extends DialogFragment {
    private static final String TAG = "IntprConfirmationDialog";

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_KEY = "EXTRA_KEY";

    private ProgressBar progressBar;
    private TkpdWebView webview;

    private boolean isProgramaticallyDismissed = false;

    public static InterruptConfirmationDialogFragment newInstance(String url) {
        InterruptConfirmationDialogFragment fragment = new InterruptConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        Log.d(TAG, "InterruptConfirmationDialogFragment newInstance = " + url);
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

            //finish activity when thanks_wallet page comes
            Uri uri = Uri.parse(url);
            Log.d(TAG, "onPageStarted: " + uri.getPath());
            if (uri.getPath().contains("thanks_wallet") || uri.getPath().contains("tokopedia.com/thanks")) {
                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_ID, "");
                intent.putExtra(EXTRA_KEY, "");
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        intent
                );
                isProgramaticallyDismissed = true;
                dismiss();
            }


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

            Uri uri = Uri.parse(urlString);
            if (uri.getScheme().equals("toko")) {
                String id = "";
                String key = "";
                if (!TextUtils.isEmpty(uri.getQueryParameter("tos_confirmation_id"))) {
                    key = "tos_confirmation_id";
                    id = uri.getQueryParameter("tos_confirmation_id");
                } else if (!TextUtils.isEmpty(uri.getQueryParameter("surge_confirmation_id"))) {
                    key = "surge_confirmation_id";
                    id = uri.getQueryParameter("surge_confirmation_id");
                }

                Log.d(TAG, "shouldOverrideUrlLoading key: " + key);
                Log.d(TAG, "shouldOverrideUrlLoading id: " + id);

                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_KEY, key);
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        intent
                );
                isProgramaticallyDismissed = true;
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

        Log.d(TAG, "InterruptDialog before seamless = " + url);

        //we need to check if the url is for tokopedia topup or activation, we need to make it seamless
        if (url != null && url.contains("tokopedia")) {
            url = URLGenerator.generateURLSessionLogin(
                    (Uri.encode(url)),
                    getActivity()
            );
        }

        Log.d(TAG, "InterruptDialog after seamless = " + url);

        progressBar.setIndeterminate(true);
        clearCache(webview);
        webview.loadAuthUrlWithFlags(url);
        webview.setWebViewClient(new InterruptConfirmationWebClient());
        webview.setWebChromeClient(new MyWebViewClient());
        WebSettings webSettings = webview.getSettings();
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
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                    // To dismiss the fragment when the back-button is pressed.
                    dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null);

            super.dismiss();
        }
    }
}
