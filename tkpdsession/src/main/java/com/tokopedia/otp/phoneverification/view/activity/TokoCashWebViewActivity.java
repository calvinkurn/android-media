package com.tokopedia.otp.phoneverification.view.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.session.R;

/**
 * @author by nisie on 6/9/17.
 */

public class TokoCashWebViewActivity extends TActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentGeneralWebView fragment =
                (FragmentGeneralWebView) getFragmentManager().findFragmentByTag(
                        FragmentGeneralWebView.class.getSimpleName());
        if (fragment == null) {
            fragment = FragmentGeneralWebView.createInstance(
                    TkpdBaseURL.URL_TOKOCASH);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public static Intent getIntentCall(Context context) {
        return new Intent(context, TokoCashWebViewActivity.class);
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
}
