package com.tokopedia.session.login.loginphonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.fragment.LoginPhoneNumberFragment;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberActivity extends TActivity implements HasComponent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (LoginPhoneNumberFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = LoginPhoneNumberFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_LOGIN_PHONE_NUMBER;
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
        return new Intent(context, LoginPhoneNumberActivity.class);
    }
}
