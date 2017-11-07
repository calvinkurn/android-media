package com.tokopedia.ride.scrooge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.ride.R;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ScroogeActivity extends AppCompatActivity {
    //callbacks URL's
    private static String ADD_CC_SUCESS_CALLBACK = "tokopedia://action_add_cc_success";
    private static String ADD_CC_FAIL_CALLBACK = "tokopedia://action_add_cc_fail";
    private static String DELETE_CC_SUCESS_CALLBACK = "tokopedia://action_delete_cc_success";
    private static String DELETE_CC_FAIL_CALLBACK = "tokopedia://action_delete_cc_fail";

    private static final String EXTRA_KEY_POST_PARAMS = "EXTRA_KEY_POST_PARAMS";
    private static final String EXTRA_KEY_URL = "URL";
    private static final String EXTRA_IS_POST_REQUEST = "EXTRA_IS_POST_REQUEST";

    private WebView mWebView;
    private ProgressBar mProgress;
    private Toolbar mToolbar;
    private Bundle mPostParams;
    private String mURl;

    private int requestCode;
    private boolean isPostRequest;

    public static Intent getCallingIntent(Context context, String url, boolean isPostRequest, Bundle postParams) {
        Intent intent = new Intent(context, ScroogeActivity.class);
        intent.putExtra(EXTRA_KEY_POST_PARAMS, postParams);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_IS_POST_REQUEST, isPostRequest);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostParams = (Bundle) getIntent().getParcelableExtra(EXTRA_KEY_POST_PARAMS);
        mURl = getIntent().getStringExtra(EXTRA_KEY_URL);
        isPostRequest = getIntent().getBooleanExtra(EXTRA_IS_POST_REQUEST, false);

        initUI();

        if (isPostRequest) {
            this.mWebView.postUrl(mURl, getPostData(mPostParams));
        } else {
            this.mWebView.loadUrl(mURl, getHeaders(mPostParams));
        }
    }

    private Map<String, String> getHeaders(Bundle mPostParams) {
        Map<String, String> headers = new ArrayMap<>();
        Set<String> keys = mPostParams.keySet();
        for (String key : keys) {
            headers.put(key, mPostParams.getString(key));
        }

        return headers;
    }

    private byte[] getPostData(Bundle mPostParams) {
        try {
            CommonUtils.dumper("ScroogeActivity :: Extracting Strings from Bundle...");
            boolean isFirstKey = true;
            StringBuffer stringBuffer = new StringBuffer();
            Iterator iterator = mPostParams.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (mPostParams.getString(key) != null) {
                    if (!isFirstKey) {
                        stringBuffer.append("&");
                    } else {
                        isFirstKey = false;
                    }
                    stringBuffer.append(URLEncoder.encode(key, "UTF-8"));
                    stringBuffer.append("=");
                    stringBuffer.append(URLEncoder.encode(mPostParams.getString(key), "UTF-8"));
                }
            }

            CommonUtils.dumper("ScroogeActivity :: URL encoded String is " + stringBuffer.toString());
            return stringBuffer.toString().getBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void initUI() {
        //create main layout
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mainLayout.setLayoutParams(linLayoutParam);


        //create and add toolbar
        mToolbar = new Toolbar(this);
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.toolbar_title_add_credit_card);
            setSupportActionBar(mToolbar);
            setToolbarColorWhite(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }
        mainLayout.addView(mToolbar);

        //create and add web view layout
        RelativeLayout webviewLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams WebLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        webviewLayout.setLayoutParams(WebLayoutParams);

        this.mWebView = new WebView(this);
        RelativeLayout.LayoutParams WebParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.mWebView.setLayoutParams(WebParams);
        setupWebView(this.mWebView);

        mProgress = new ProgressBar(this, null, android.R.style.Widget_ProgressBar_Horizontal);
        RelativeLayout.LayoutParams progressbarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressbarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mProgress.setLayoutParams(progressbarParams);
        mProgress.setIndeterminate(true);

        webviewLayout.addView(this.mWebView);
        webviewLayout.addView(mProgress);

        mainLayout.addView(webviewLayout);

        setContentView(mainLayout);
    }

    protected void setToolbarColorWhite(Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }
        int textColor = ContextCompat.getColor(this, R.color.white);
        toolbar.setTitleTextColor(textColor);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText);
        toolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleText);
        toolbar.setSubtitleTextColor(textColor);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    /**
     * Set up web view and register webview client
     *
     * @param webview
     */
    private void setupWebView(WebView webview) {
        if (webview == null) return;

        webview.setWebViewClient(new WebViewClient() {
            public synchronized void onPageStarted(WebView inView, String inUrl, Bitmap inFavicon) {
                super.onPageStarted(inView, inUrl, inFavicon);
                CommonUtils.dumper("ScroogeActivity :: onPageStarted url " + inUrl);
                Intent responseIntent = new Intent();
                if (inUrl.equalsIgnoreCase(ADD_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_SUCCESS, responseIntent);
                    finish();
                } else if (inUrl.equalsIgnoreCase(ADD_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Fail");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_FAIL, responseIntent);
                    finish();
                } else if (inUrl.equalsIgnoreCase(DELETE_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL, responseIntent);
                    finish();
                } else if (inUrl.equalsIgnoreCase(DELETE_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "FAIL");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_SUCCESS, responseIntent);
                    finish();
                }
            }

            public synchronized void onPageFinished(WebView inView, String inUrl) {
                super.onPageFinished(inView, inUrl);
                CommonUtils.dumper("ScroogeActivity :: onPageFinished url " + inUrl);
            }

            public synchronized void onReceivedError(WebView inView, int iniErrorCode, String inDescription, String inFailingUrl) {
                super.onReceivedError(inView, iniErrorCode, inDescription, inFailingUrl);
                CommonUtils.dumper("ScroogeActivity :: Error occured while loading url " + inFailingUrl);
                Intent responseIntent = new Intent();
                responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, inDescription);
                setResult(ScroogePGUtil.RESULT_CODE_RECIEVED_ERROR, responseIntent);
                finish();
            }

            public synchronized void onReceivedSslError(WebView inView, SslErrorHandler inHandler, SslError inError) {
                CommonUtils.dumper("ScroogeActivity :: SSL Error occured " + inError.toString());
                CommonUtils.dumper("ScroogeActivity :: SSL Handler is " + inHandler);
                if (inHandler != null) {
                    inHandler.proceed();
                }
            }
        });

        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptEnabled(true);
    }
}
