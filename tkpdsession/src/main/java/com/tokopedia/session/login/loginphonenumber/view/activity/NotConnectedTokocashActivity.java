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
import com.tokopedia.session.login.loginphonenumber.view.fragment.NotConnectedTokocashFragment;


/**
 * @author by nisie on 12/4/17.
 */

public class NotConnectedTokocashActivity extends TActivity implements HasComponent {

    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_TYPE = "type";
    public static final int TYPE_PHONE_NOT_CONNECTED = 1;
    public static final int TYPE_NO_TOKOCASH_ACCOUNT = 2;

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
                (NotConnectedTokocashFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = NotConnectedTokocashFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_NOT_CONNECTED_TO_TOKOCASH;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if(getIntent().getExtras()!= null && getIntent().getExtras().getInt(PARAM_TYPE) ==
                TYPE_PHONE_NOT_CONNECTED){
            toolbar.setTitle(R.string.phone_number_not_connected);
        }else if(getIntent().getExtras()!= null && getIntent().getExtras().getInt(PARAM_TYPE) ==
                TYPE_NO_TOKOCASH_ACCOUNT){
            toolbar.setTitle(R.string.no_account_connected);
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getPhoneNumberNotConnectedIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, NotConnectedTokocashActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_TYPE, TYPE_PHONE_NOT_CONNECTED);
        return intent;
    }

    public static Intent getNoTokocashAccountIntent(Context context, String phoneNumber) {
        Intent intent = new Intent(context, NotConnectedTokocashActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_TYPE, TYPE_NO_TOKOCASH_ACCOUNT);
        return intent;
    }
}
