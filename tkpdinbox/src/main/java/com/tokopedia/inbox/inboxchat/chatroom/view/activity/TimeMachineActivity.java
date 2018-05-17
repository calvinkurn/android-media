package com.tokopedia.inbox.inboxchat.chatroom.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.inbox.R;

/**
 * @author by nisie on 10/25/17.
 */

public class TimeMachineActivity extends TActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {

    private static final String ARGS_URL = "ARGS_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);

        String url = getIntent().getExtras().getString(ARGS_URL, TkpdBaseURL.User.URL_INBOX_MESSAGE_TIME_MACHINE);
        initView(url);
    }

    private void initView(String url) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FragmentGeneralWebView fragment =
                (FragmentGeneralWebView) getFragmentManager().findFragmentByTag(
                        FragmentGeneralWebView.class.getSimpleName());
        if (fragment == null) {
            fragment = FragmentGeneralWebView.createInstance(url);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
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
        Intent intent = new Intent(context, TimeMachineActivity.class);
        intent.putExtra(ARGS_URL, url);
        return intent;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
