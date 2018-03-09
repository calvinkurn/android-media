package com.tokopedia.inbox.contactus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core.util.TkpdWebViewClient;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.activity.ContactUsActivity.BackButtonListener;

import butterknife.BindView;

import static com.tokopedia.inbox.contactus.ContactUsConstant.PARAM_URL;

/**
 * Created by nisie on 8/12/16.
 */
public class ContactUsFaqFragment extends BasePresenterFragment {

    private static final String SOLUTION_ID = "solution_id";
    private static final String TAGS = "tags";
    private static final String ORDER_ID = "order_id";
    private static final String APPLINK_SCHEME = "tokopedia://";

    @BindView(R2.id.scroll_view)
    ScrollView mainView;

    @BindView(R2.id.webview)
    TkpdWebView webView;

    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    ContactUsFaqListener listener;
    String url;

    public interface ContactUsFaqListener {
        void onGoToCreateTicket(Bundle solutionId);
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
    protected void onFirstTimeLaunched() {
        String url;
        if (getArguments().getString(PARAM_URL, "").equals("")) {
            url = TkpdBaseURL.ContactUs.URL_HELP;
        } else
            url = getArguments().getString(PARAM_URL);

        webView.loadAuthUrlWithFlags(url);


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
        MethodChecker.setAllowMixedContent(webSettings);
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

    private class MyWebClient extends TkpdWebViewClient {
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
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mainView != null)
                mainView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mainView != null)
                            mainView.smoothScrollTo(0, 0);
                    }
                }, 300);
        }

        @Override
        protected boolean onOverrideUrl(Uri url) {
            try {
                if (url.getLastPathSegment().equals("contact-us.pl")) {
                    webView.loadAuthUrlWithFlags(URLGenerator.generateURLContactUs(TkpdBaseURL.BASE_CONTACT_US, context));
                    return true;
                } else if (url.getQueryParameter("action") != null &&
                        url.getQueryParameter("action").equals("create_ticket")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactUsActivity.PARAM_SOLUTION_ID,
                            url.getQueryParameter(SOLUTION_ID) == null ? "" : url.getQueryParameter(SOLUTION_ID));
                    bundle.putString(ContactUsActivity.PARAM_TAG,
                            url.getQueryParameter(TAGS) == null ? "" : url.getQueryParameter(TAGS));
                    bundle.putString(ContactUsActivity.PARAM_ORDER_ID,
                            url.getQueryParameter(ORDER_ID) == null ? "" : url.getQueryParameter(ORDER_ID));
                    listener.onGoToCreateTicket(bundle);
                    return true;
                } else if (url.getQueryParameter("action") != null &&
                        url.getQueryParameter("action").equals("return")) {
                    CommonUtils.UniversalToast(getActivity(), getString(R.string.finish_contact_us));
                    getActivity().finish();
                    return true;
                } else if (url.toString().contains(APPLINK_SCHEME)
                        && getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
                    ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(), url.toString(), new Bundle());
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