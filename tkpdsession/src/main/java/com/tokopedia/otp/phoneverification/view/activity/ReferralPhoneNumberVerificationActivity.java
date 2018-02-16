package com.tokopedia.otp.phoneverification.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.phoneverification.view.fragment.ReferralPhoneNumberVerificationFragment;
import com.tokopedia.session.R;

public class ReferralPhoneNumberVerificationActivity extends BasePresenterActivity {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, ReferralPhoneNumberVerificationActivity.class);
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
        return R.layout.activity_referral_phone_number_verification;
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
        addFragment(R.id.container, ReferralPhoneNumberVerificationFragment.createInstance(getReferralPhoneVerificationListener()));
    }

    private void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                ReferralPhoneNumberVerificationActivity.this.setResult(Activity.RESULT_CANCELED);
                ReferralPhoneNumberVerificationActivity.this.finish();
            }


            @Override
            public void onSuccessVerification() {
                ReferralPhoneNumberVerificationActivity.this.setResult(Activity.RESULT_OK);
                ReferralPhoneNumberVerificationActivity.this.finish();
            }
        };
    }

    private ReferralPhoneNumberVerificationFragment.ReferralPhoneNumberVerificationFragmentListener getReferralPhoneVerificationListener() {
        return new ReferralPhoneNumberVerificationFragment.ReferralPhoneNumberVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {

            }

            @Override
            public void onClickVerification(String phoneNumber) {
                addFragment(R.id.container, PhoneVerificationFragment.createInstance(getPhoneVerificationListener(), phoneNumber));

            }
        };
    }
}