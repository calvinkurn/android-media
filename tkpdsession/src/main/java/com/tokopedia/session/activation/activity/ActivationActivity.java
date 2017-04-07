package com.tokopedia.session.activation.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.fragment.RegisterActivationFragment;

/**
 * Created by nisie on 2/1/17.
 */

public class ActivationActivity extends BasePresenterActivity {

    private static final String INTENT_EXTRA_PARAM_EMAIL = "INTENT_EXTRA_PARAM_EMAIL";
    private static final String INTENT_EXTRA_PARAM_NAME = "INTENT_EXTRA_PARAM_NAME";

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

        RegisterActivationFragment fragment = RegisterActivationFragment.createInstance(
                getIntent().getExtras().getString(INTENT_EXTRA_PARAM_EMAIL),
                getIntent().getExtras().getString(INTENT_EXTRA_PARAM_NAME)
        );
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
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
        return null;
    }

    public static Intent getCallingIntent(Context context, String email, String name) {
        Intent callingIntent = new Intent(context, ActivationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_NAME, name);
        return callingIntent;
    }

    public static Intent getCallingIntent(Context context, String email) {
        Intent callingIntent = new Intent(context, ActivationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_NAME, "");
        return callingIntent;
    }
}
