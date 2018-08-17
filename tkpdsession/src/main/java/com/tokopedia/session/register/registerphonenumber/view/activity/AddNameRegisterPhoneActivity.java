package com.tokopedia.session.register.registerphonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.view.fragment.AddNameRegisterPhoneFragment;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNameRegisterPhoneActivity extends BasePresenterActivity implements HasComponent {

    public static final String PARAM_PHONE = "param_phone";
    private RegisterAnalytics analytics;

    public static Intent newInstance(Context context, String phoneNumber) {
        Intent intent = new Intent(context, AddNameRegisterPhoneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE, phoneNumber);
        intent.putExtras(bundle);
        return intent;
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
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag
                (AddNameRegisterPhoneFragment.class.getSimpleName());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            fragment = AddNameRegisterPhoneFragment.newInstance(bundle);
        }
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = RegisterAnalytics.initAnalytics(this);
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onBackPressed() {
        analytics.eventClickBackAddName();
        super.onBackPressed();
    }
}
