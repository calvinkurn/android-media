package com.tokopedia.session.register.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.fragment.RegisterEmailFragment;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterEmailActivity extends BasePresenterActivity implements HasComponent {

    public static final String EXTRA_PARAM_EMAIL = "email";
    private String email;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        RegisterEmailFragment fragment;
        if (getIntent().getExtras() != null) {
            fragment = RegisterEmailFragment.createInstanceWithEmail(getIntent().getExtras());
        } else {
            fragment = RegisterEmailFragment.createInstance();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass()
                    .getSimpleName());
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterEmailActivity.class);
    }

    public static Intent getCallingIntentWithEmail(@NonNull Context context, @NonNull String email) {
        Intent intent = new Intent(context, RegisterEmailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_EMAIL, email);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }
}
