package com.tokopedia.session.login.loginemail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.applink.SessionApplinkUrl;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginemail.view.fragment.LoginFragment;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginActivity extends TActivity implements HasComponent {

    public static final int METHOD_FACEBOOK = 111;
    public static final int METHOD_GOOGLE = 222;
    public static final int METHOD_WEBVIEW = 333;
    public static final int METHOD_EMAIL = 444;

    public static final String AUTO_WEBVIEW_NAME = "webview_name";
    public static final String AUTO_WEBVIEW_URL = "webview_url";

    @DeepLink({SessionApplinkUrl.LOGIN})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        if (SessionHandler.isV4Login(context)) {
            if (context.getApplicationContext() instanceof SessionRouter) {
                return ((SessionRouter) context.getApplicationContext()).getHomeIntent(context);
            } else {
                throw new RuntimeException("Applinks intent unsufficient");
            }
        } else {
            Intent intent = getCallingIntent(context);
            return intent.setData(uri.build());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }


    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null) {
            bundle.putAll(getIntent().getExtras());
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (LoginFragment.class.getSimpleName());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = LoginFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    @Override
    public String getScreenName() {
        return LoginAnalytics.Screen.LOGIN;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Intent getAutoLoginGoogle(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_GOOGLE);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAutoLoginFacebook(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_FACEBOOK);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getAutoLoginWebview(Context context, String name, String url) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_WEBVIEW);
        bundle.putString(AUTO_WEBVIEW_NAME, name);
        bundle.putString(AUTO_WEBVIEW_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setPadding(0, 0, 30, 0);
    }

    public static Intent getAutomaticLogin(Context context, String email, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(LoginFragment.IS_AUTO_LOGIN, true);
        bundle.putInt(LoginFragment.AUTO_LOGIN_METHOD, METHOD_EMAIL);
        bundle.putString(LoginFragment.AUTO_LOGIN_EMAIL, email);
        bundle.putString(LoginFragment.AUTO_LOGIN_PASS, password);
        intent.putExtras(bundle);
        return intent;
    }

}
