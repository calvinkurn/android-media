package com.tokopedia.transaction.pickuppoint.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.pickuppoint.domain.model.Store;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.transaction.pickuppoint.view.contract.PickupPointContract.Constant.INTENT_DATA_STORE;

public class PickupPointMapActivity extends BasePresenterActivity {

    private static final String MAP_PARAM_WIDTH = "640";
    private static final String MAP_PARAM_HEIGHT = "640";
    private static final String MAP_PARAM_ZOOM = "17";
    private static final String MAP_PARAM_TYPE = "roadmap";

    @BindView(R2.id.web_view_pickup_booth_location)
    WebView webViewPickupBoothLocation;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R2.id.btn_choose_pickup_booth)
    Button btnChoosePickupBooth;

    private Store store;

    public static Intent createInstance(Activity activity, Store store) {
        Intent intent = new Intent(activity, PickupPointMapActivity.class);
        intent.putExtra(INTENT_DATA_STORE, store);
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
        if (getIntent().getParcelableExtra(INTENT_DATA_STORE) != null) {
            store = getIntent().getParcelableExtra(INTENT_DATA_STORE);
            Log.e("Store1", store.toString());
            setupWebView();
        }
    }

    private void setupWebView() {
        webViewPickupBoothLocation.getSettings().setLoadWithOverviewMode(true);
        webViewPickupBoothLocation.getSettings().setUseWideViewPort(true);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - toolbar.getHeight();
        Log.e(String.valueOf(width), String.valueOf(height));
        pbLoading.setVisibility(View.VISIBLE);
        String url = String.format(getString(com.tokopedia.transaction.R.string.url_static_map),
                store.getGeolocation(), MAP_PARAM_ZOOM, MAP_PARAM_HEIGHT, MAP_PARAM_WIDTH,
                MAP_PARAM_TYPE, store.getGeolocation(), getString(R.string.google_api_key));
        Log.e("MapUrl", url);
        webViewPickupBoothLocation.setWebViewClient(new TermsAndConditionsWebViewClient());
        webViewPickupBoothLocation.setWebChromeClient(new WebChromeClient());
        webViewPickupBoothLocation.loadUrl(url);
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

    @OnClick(R2.id.btn_choose_pickup_booth)
    public void onChoosePickupBooth() {
        setResult(Activity.RESULT_OK, getIntent());
        finish();
    }

}