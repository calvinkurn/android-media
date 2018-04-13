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
import com.tokopedia.session.login.loginphonenumber.view.fragment.ChooseTokocashAccountFragment;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountActivity extends TActivity implements HasComponent {

    public static final String ARGS_DATA = "data";

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
                (ChooseTokocashAccountFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = ChooseTokocashAccountFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    @Override
    public String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context, ChooseTokoCashAccountViewModel model) {
        Intent intent = new Intent(context, ChooseTokocashAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_DATA, model);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        toolbar.setPadding(0, 0, 16, 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }
}
