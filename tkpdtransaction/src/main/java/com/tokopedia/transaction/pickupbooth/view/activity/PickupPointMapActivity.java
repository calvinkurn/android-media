package com.tokopedia.transaction.pickupbooth.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;

import butterknife.BindView;

public class PickupPointMapActivity extends BasePresenterActivity {

    public static final String INTENT_DATA_GEOLOCATION = "geolocation";

    @BindView(R2.id.web_view_pickup_booth_location)
    WebView webViewPickupBoothLocation;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;

    public static Intent createInstance(Activity activity, String geolocation) {
        Intent intent = new Intent(activity, PickupPointMapActivity.class);
        intent.putExtra(INTENT_DATA_GEOLOCATION, geolocation);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pickup_booth_map;
    }

    @Override
    protected void initView() {
        if (getIntent().getStringExtra(INTENT_DATA_GEOLOCATION) != null) {
            setupWebView(getIntent().getStringExtra(INTENT_DATA_GEOLOCATION));
        }
    }

    private void setupWebView(String geolocation) {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - toolbar.getHeight();
        pbLoading.setVisibility(View.VISIBLE);
        webViewPickupBoothLocation.setWebViewClient(new TermsAndConditionsWebViewClient());
        webViewPickupBoothLocation.setWebChromeClient(new WebChromeClient());
        webViewPickupBoothLocation.loadUrl("http://maps.googleapis.com/maps/api/staticmap?\n" +
                "center=" + geolocation + "&\n" +
                "zoom=17&\n" +
                "size=" + width + "x" + height + "&\n" +
                "maptype=roadmap&\n" +
                "markers=" + geolocation + "&\n" +
                "key=" + getString(R.string.google_api_key));
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(com.tokopedia.core.R.drawable.ic_clear_24dp);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    private class TermsAndConditionsWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pbLoading.setVisibility(View.GONE);
        }

    }

}