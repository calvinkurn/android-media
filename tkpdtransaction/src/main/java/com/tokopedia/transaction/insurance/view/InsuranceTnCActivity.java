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

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_LOYALTY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragmentWebView();
    }

    private void showFragmentWebView() {
        Fragment fragment = new InsuranceTnCFragment();
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
