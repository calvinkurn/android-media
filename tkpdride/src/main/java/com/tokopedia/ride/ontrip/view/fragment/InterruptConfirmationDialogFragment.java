package com.tokopedia.ride.ontrip.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.ride.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
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
            if (urlString.endsWith("action_back") || (uri.getScheme().equals("tokopedia") && uri.getLastPathSegment().equalsIgnoreCase("back"))) {
                isProgramaticallyDismissed = true;
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null);
                dismiss();

            } else if (uri.getScheme().equals("tokopedia") && uri.getHost().equalsIgnoreCase("digital")) {
                Map<String, String> maps = splitQuery(uri);
                Bundle bundle = new Bundle();
                for (Map.Entry<String, String> imap : maps.entrySet()) {
                    bundle.putString(imap.getKey(), imap.getValue());
                }
                IDigitalModuleRouter digitalModuleRouter = (IDigitalModuleRouter) getActivity().getApplication();
                InterruptConfirmationDialogFragment.this.startActivityForResult(
                        digitalModuleRouter.instanceIntentCartDigitalProductWithBundle(bundle),
                        IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
                );
            } else if (uri.getScheme().equals("tokopedia")) {
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

    private Map<String, String> splitQuery(Uri url) {
        Map<String, String> queryPairs = new LinkedHashMap<>();
        String query = url.getQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] pairs = query.split("&|\\?");
            for (String pair : pairs) {
                int indexKey = pair.indexOf("=");
                if (indexKey > 0 && indexKey + 1 <= pair.length()) {
                    try {
                        queryPairs.put(URLDecoder.decode(pair.substring(0, indexKey), "UTF-8"),
                                URLDecoder.decode(pair.substring(indexKey + 1), "UTF-8"));
                    } catch (UnsupportedEncodingException | NullPointerException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return queryPairs;
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
            clearCache(webview);
            webview.loadAuthUrlWithFlags(url);
        } else {
            clearCache(webview);
            webview.loadUrl(url);
        }

        Log.d(TAG, "InterruptDialog after seamless = " + url);

        progressBar.setIndeterminate(true);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (resultCode == IDigitalModuleRouter.PAYMENT_SUCCESS) {
                    if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                        String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                        if (!TextUtils.isEmpty(message)) {
                            NetworkErrorHelper.showSnackbar(getActivity(), message);
                        }
                    }
                    Intent intent = getActivity().getIntent();
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_OK,
                            intent
                    );
                    isProgramaticallyDismissed = true;
                    dismiss();
                } else {
                    isProgramaticallyDismissed = true;
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(),
                            Activity.RESULT_CANCELED,
                            null);
                    dismiss();
                }

                break;
        }
    }
}
