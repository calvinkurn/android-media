package com.tokopedia.otp.phoneverification.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationProfileFragment;
import com.tokopedia.session.R;

/**
 * Created by nisie on 2/23/17.
 */

public class PhoneVerificationProfileActivity extends BasePresenterActivity {
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
        return R.layout.activity_phone_verification_profile;
    }

    @Override
    protected void initView() {


        PhoneVerificationProfileFragment fragmentHeader = PhoneVerificationProfileFragment.createInstance();
        PhoneVerificationFragment fragment = PhoneVerificationFragment.createInstance(getPhoneVerificationListener());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(R.id.container_header) == null) {
            fragmentTransaction.add(R.id.container_header, fragmentHeader, fragmentHeader.getClass().getSimpleName());
        }
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();

    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
            @Override
            public void onSkipVerification() {
                if (isTaskRoot()) {
                    Intent intent = new Intent(PhoneVerificationProfileActivity.this,
                            ManagePeopleProfileActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }


            @Override
            public void onSuccessVerification() {
                if (isTaskRoot()) {
                    Intent intent = new Intent(PhoneVerificationProfileActivity.this,
                            ManagePeopleProfileActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        };
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
