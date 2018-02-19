package com.tokopedia.session.register.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.CreatePasswordFragment;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

/**
 * @author by nisie on 10/12/17.
 */

public class CreatePasswordActivity extends TActivity implements HasComponent {

    public static final String ARGS_FORM_DATA = "ARGS_FORM_DATA";

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
                (CreatePasswordFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = CreatePasswordFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    public String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_CREATE_PASSWORD;
    }

    public static Intent getCallingIntent(Context context, CreatePasswordViewModel createPasswordViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_FORM_DATA, createPasswordViewModel);
        Intent intent = new Intent(context, CreatePasswordActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
