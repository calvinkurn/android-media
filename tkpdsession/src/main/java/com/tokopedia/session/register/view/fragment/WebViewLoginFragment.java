package com.tokopedia.session.register.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.util.TkpdWebView;

import java.util.Set;

/**
 * Created by stevenfredian on 5/31/16.
 */
public class WebViewLoginFragment extends android.support.v4.app.DialogFragment {
    //private View pic;

    String url;


    public static WebViewLoginFragment createInstance(String url){
        WebViewLoginFragment fragment = new WebViewLoginFragment();
        fragment.url = url;
        return fragment;
    }

    public WebViewLoginFragment()
    {
    }

    private TkpdWebView webViewOauth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_webview_login, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        webViewOauth = (TkpdWebView) view.findViewById(R.id.web_oauth);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        // Fetch arguments from bundle and set title
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoginWebview);
        CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webViewOauth.clearCache(true);
        webViewOauth.loadAuthUrlWithFlags(url);
        webViewOauth.setWebViewClient(new AuthWebClient());
        webViewOauth.clearHistory();
        webViewOauth.clearFormData();
        webViewOauth.requestFocus(View.FOCUS_DOWN);
        webViewOauth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        //activates JavaScript (just in case)
        WebSettings webSettings = webViewOauth.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private class AuthWebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //check if the login was successful and the access token returned
            //this test depend of your API
            return parseUrl(url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Bundle bundle = new Bundle();
            bundle.putString("error", String.valueOf(error));
            Intent intent = new Intent();
            intent.putExtra("bundle",bundle);
//            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
            SnackbarManager.make(getActivity(),getResources().getString(R.string.msg_network_error),Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean parseUrl(String url) {
        Log.d("steven check " , url);
        Uri uri = Uri.parse(url);
        String protocol = uri.getScheme();
        String server = uri.getAuthority();
        String path = uri.getPath();
        Set<String> args = uri.getQueryParameterNames();
        if(server.startsWith("accounts")&& server.endsWith("tokopedia.com") &&
                (path.contains("code")||path.contains("error") || path.contains("activation-social"))){
            Bundle bundle = new Bundle();
            bundle.putString("server",server);
            bundle.putString("path",path);
            for(String arg : args){
                String limit = uri.getQueryParameter(arg);
                bundle.putString(arg,limit);
            }
            Intent intent = new Intent();
            intent.putExtra("bundle",bundle);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (webViewOauth.canGoBack()) {
                        webViewOauth.goBack();
                        return true;
                    } else {
                        dismiss();
                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getTargetFragment().isVisible()) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
        }
        super.onDismiss(dialog);
    }
}