package com.tokopedia.session.register.registerphonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.view.fragment.RegisterPhoneNumberFragment;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberActivity extends BasePresenterActivity implements HasComponent {


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, RegisterPhoneNumberActivity.class);
    }

    @Override
    public void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (RegisterPhoneNumberFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = RegisterPhoneNumberFragment.createInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }


    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }


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
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
