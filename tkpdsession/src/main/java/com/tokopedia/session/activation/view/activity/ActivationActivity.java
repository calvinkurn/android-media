package com.tokopedia.session.activation.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.Toolbar;

import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.fragment.RegisterActivationFragment;

/**
 * Created by nisie on 2/1/17.
 */

public class ActivationActivity extends BasePresenterActivity {

    public static final String INTENT_EXTRA_PARAM_EMAIL = "email";
    public static final String INTENT_EXTRA_PARAM_PW = "pw";
    private RegisterAnalytics analytics;

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

        setToolbar();

        RegisterActivationFragment fragment = RegisterActivationFragment.createInstance(
                getIntent().getExtras()
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = RegisterAnalytics.initAnalytics(this);
    }

    private void setToolbar() {

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }


        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_action_back);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
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

    @Override
    public String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_ACCOUNT_ACTIVATION;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ScreenTracking.screen(this,getScreenName());
    }

    public static Intent getCallingIntent(Context context, String email, String pw) {
        Intent callingIntent = new Intent(context, ActivationActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_PW, pw);
        return callingIntent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        analytics.eventClickBackEmailActivation();
    }
}
