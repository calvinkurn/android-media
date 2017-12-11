package com.tokopedia.transaction.insurance.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.transaction.R;

public class InsuranceTnCActivity extends TActivity implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String EXTRA_URL = "url";
    private String url;

    public static Intent createInstance(Activity activity, String url) {
        Intent intent = new Intent(activity, InsuranceTnCActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_LOYALTY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_URL)) {
            url = getIntent().getExtras().getString(EXTRA_URL);
            showFragmentWebView();
        }
    }

    private void showFragmentWebView() {
        Fragment fragment = FragmentGeneralWebView.createInstance(url, false);
        getFragmentManager().beginTransaction().add(R.id.parent_view, fragment).commit();
    }

    @Override
    public void onWebViewSuccessLoad() {
    }

    @Override
    public void onWebViewErrorLoad() {
    }

    @Override
    public void onWebViewProgressLoad() {
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_clear_24dp);
    }
}
