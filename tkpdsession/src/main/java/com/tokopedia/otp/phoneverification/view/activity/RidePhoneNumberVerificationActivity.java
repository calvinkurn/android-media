package com.tokopedia.otp.phoneverification.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.session.R;

public class RidePhoneNumberVerificationActivity extends BasePresenterActivity {
    public static final int RIDE_PHONE_VERIFY_REQUEST_CODE = 1011;

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, RidePhoneNumberVerificationActivity.class);
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
        return R.layout.activity_ride_phone_number_verification;
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
        addFragment(R.id.container, PhoneVerificationFragment.createInstance
                (getPhoneVerificationListener(), false));
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                RidePhoneNumberVerificationActivity.this.setResult(Activity.RESULT_CANCELED);
                RidePhoneNumberVerificationActivity.this.finish();
            }


            @Override
            public void onSuccessVerification() {
                RidePhoneNumberVerificationActivity.this.setResult(Activity.RESULT_OK);
                RidePhoneNumberVerificationActivity.this.finish();
            }
        };
    }
}
