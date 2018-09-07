package com.tokopedia.session.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberWarningFragment;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberWarningActivityListener;

public class ChangePhoneNumberWarningActivity extends BasePresenterActivity
        implements ChangePhoneNumberWarningActivityListener.View, HasComponent {
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE_NUMBER = "phone_number";

    private String email;
    private String phoneNumber;

    public static Intent newInstance(Context context, String email, String phoneNumber) {
        Intent intent = new Intent(context, ChangePhoneNumberWarningActivity.class);
        intent.putExtra(PARAM_EMAIL, email);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        email = extras.getString(PARAM_EMAIL);
        phoneNumber = extras.getString(PARAM_PHONE_NUMBER);
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
        String TAG = ChangePhoneNumberWarningFragment.class.getSimpleName();
        ChangePhoneNumberWarningFragment fragment = ChangePhoneNumberWarningFragment.newInstance
                (email, phoneNumber);

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

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CHANGE_PHONE_NUMBER_WARNING;
    }
}
