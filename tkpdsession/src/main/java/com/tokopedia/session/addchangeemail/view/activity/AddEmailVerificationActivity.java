package com.tokopedia.session.addchangeemail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangeemail.view.fragment.AddEmailVerificationFragment;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailVerificationActivity extends BasePresenterActivity implements HasComponent {

    public static final String INTENT_EXTRA_PARAM_EMAIL = "email";
    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_MESSAGE = "message";


    public static Intent newInstance(Context context, String email) {
        Intent callingIntent = new Intent(context, AddEmailVerificationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        return callingIntent;
    }

    @Override
    public Object getComponent() {
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
    protected void initView() {
        String TAG = AddEmailVerificationFragment.class.getSimpleName();
        AddEmailVerificationFragment fragment = AddEmailVerificationFragment.newInstance(
                intentBundle(getIntent().getExtras().getString(INTENT_EXTRA_PARAM_EMAIL)));
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
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    private Bundle intentBundle(String email) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_email);
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_MESSAGE, createEmailMessage(email));
        return bundle;
    }

    private String createEmailMessage(String email) {
        if (!TextUtils.isEmpty(email)) {
            return getString(R.string.verification_code_email_sent_to) + " " + email;
        } else {
            return "";
        }
    }
}
