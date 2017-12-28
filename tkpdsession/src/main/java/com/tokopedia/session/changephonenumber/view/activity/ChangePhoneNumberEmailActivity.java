package com.tokopedia.session.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailFragment;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailActivityListener;

public class ChangePhoneNumberEmailActivity extends BasePresenterActivity
        implements ChangePhoneNumberEmailActivityListener.View, HasComponent {
    public static final String PARAM_EMAIL = "email";

    private String email;

    public static Intent newInstance(Context context, String email) {
        Intent intent = new Intent(context, ChangePhoneNumberEmailActivity.class);
        intent.putExtra(PARAM_EMAIL, email);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        email = extras.getString(PARAM_EMAIL);
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
        inflateFragment();
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
    public void inflateFragment() {
        String TAG = ChangePhoneNumberEmailFragment.class.getSimpleName();
        ChangePhoneNumberEmailFragment fragment = ChangePhoneNumberEmailFragment.newInstance(email);

        if (getSupportFragmentManager().findFragmentByTag(TAG) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(com.tokopedia.core.R.id.container,
                            getSupportFragmentManager().findFragmentByTag(TAG))
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(com.tokopedia.core.R.id.container, fragment, TAG)
                    .commit();
        }

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }
}
