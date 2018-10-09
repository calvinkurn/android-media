package com.tokopedia.otp.phoneverification.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationProfileFragment;
import com.tokopedia.otp.phoneverification.view.fragment.ReferralPhoneNumberVerificationFragment;
import com.tokopedia.session.R;

public class ReferralPhoneNumberVerificationActivity extends TActivity implements HasComponent {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, ReferralPhoneNumberVerificationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_referral_phone_number_verification);
        initView();
    }

    protected void initView() {
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
                PhoneVerificationProfileFragment fragmentHeader = PhoneVerificationProfileFragment.createInstance();
                PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance
                        (getPhoneVerificationListener(), false);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
                    fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
                }
                if (getFragmentManager().findFragmentById(R.id.container) == null) {
                    fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
                } else if (((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getListener() == null) {
                    ((PhoneVerificationFragment) getSupportFragmentManager().findFragmentById(R.id.container))
                            .setPhoneVerificationListener(getPhoneVerificationListener());
                }
                fragmentTransaction.commit();
            }
        };
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}