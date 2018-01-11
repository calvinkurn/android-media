package com.tokopedia.session.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.fragment.ChangePhoneNumberEmailFragment;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberEmailActivityListener;

import java.util.ArrayList;

/**
 * Created by milhamj on 11/01/18.
 */

public class ChangePhoneNumberEmailActivity extends BasePresenterActivity
        implements ChangePhoneNumberEmailActivityListener.View, HasComponent {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";

    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;

    public static Intent newInstance(Context context, String phoneNumber, String email,
                                     ArrayList<String> warningList) {
        Intent intent = new Intent(context, ChangePhoneNumberEmailActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_WARNING_LIST, warningList);
        intent.putExtra(PARAM_EMAIL, email);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        phoneNumber = extras.getString(PARAM_PHONE_NUMBER);
        warningList = extras.getStringArrayList(PARAM_WARNING_LIST);
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
        ChangePhoneNumberEmailFragment fragment =
                ChangePhoneNumberEmailFragment.newInstance(phoneNumber,
                        email,
                        warningList);

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
