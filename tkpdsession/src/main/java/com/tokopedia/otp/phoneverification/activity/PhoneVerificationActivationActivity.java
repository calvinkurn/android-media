package com.tokopedia.otp.phoneverification.activity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationActivationFragment;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationActivationActivity extends BasePresenterActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_verification_activation;
    }

    @Override
    protected void initView() {

        PhoneVerificationActivationFragment fragmentHeader = PhoneVerificationActivationFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        fragmentTransaction.commit();

        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance();
        FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction2.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction2.commit();
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
