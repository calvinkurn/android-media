package com.tokopedia.loyalty.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.loyalty.view.fragment.TokoPointWebViewFragment;

/**
 * @author okasurya on 1/29/18.
 */

public class TokoPointWebviewActivity extends TkpdCoreWebViewActivity {
    public static final String EXTRA_URL = "url";
    private TokoPointWebViewFragment fragment;

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, TokoPointWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getIntentWithTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, TokoPointWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(com.tokopedia.core.R.layout.activity_webview_container);
        String url = getIntent().getExtras().getString(EXTRA_URL);
        fragment = TokoPointWebViewFragment.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(com.tokopedia.core.R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment.getWebview().canGoBack()) {
                fragment.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
