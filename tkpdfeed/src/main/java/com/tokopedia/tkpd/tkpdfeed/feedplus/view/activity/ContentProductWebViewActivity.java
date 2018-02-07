package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * @author by yfsx on 1/11/18.
 */

public class ContentProductWebViewActivity extends TActivity implements
        FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String EXTRA_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

        if (fragment == null || !(fragment instanceof FragmentGeneralWebView))
            fragment = FragmentGeneralWebView.createInstance(url);

        getFragmentManager().beginTransaction().replace(R.id.container,
                fragment).commit();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
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

    public static Intent getCallingIntent(Context context, String url) {
        Intent intent = new Intent(context, BannerWebView.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }
}
