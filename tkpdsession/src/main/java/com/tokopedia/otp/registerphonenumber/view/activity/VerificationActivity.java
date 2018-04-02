package com.tokopedia.otp.registerphonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.OTPAnalytics;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.registerphonenumber.view.fragment.VerificationFragment;
import com.tokopedia.otp.registerphonenumber.view.fragment.VerificationMethodFragment;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.MethodItem;
import com.tokopedia.session.R;

import java.util.ArrayList;

/**
 * @author by yfsx on 5/3/18.
 */

public class VerificationActivity extends BasePresenterActivity implements HasComponent {

    public static final int TYPE_SMS = 1;
    public static final int TYPE_PHONE_CALL = 2;

    public static final String PARAM_FRAGMENT_TYPE = "type";
    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_APP_SCREEN = "app_screen";
    public static final String PARAM_METHOD_LIST = "method_list";

    private static final String FIRST_FRAGMENT_TAG = "first";
    private static final String CHOOSE_FRAGMENT_TAG = "choose";

    @Override
    public void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        Fragment fragment;
        fragment = getFragment(getIntent().getExtras().getInt(PARAM_FRAGMENT_TYPE, -1), bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        fragmentTransaction.commit();

        Log.d("NISNIS", getSupportFragmentManager().getBackStackEntryCount() + " " + (
                getSupportFragmentManager().getBackStackEntryCount() > 0 ? getSupportFragmentManager()
                        .getBackStackEntryAt
                                (getSupportFragmentManager()
                                        .getBackStackEntryCount() - 1).getName() : "No fragment at " +
                        "backstack"));
    }

    private Fragment getFragment(int type, Bundle bundle) {
        Fragment fragment;
        switch (type) {
            case TYPE_SMS: {
                String phoneNumber = bundle.getString(PARAM_PHONE_NUMBER, "");
                fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber));
                break;
            }
            default: {
                fragment = VerificationMethodFragment.createInstance(bundle);
                break;
            }
        }
        return fragment;
    }

    private String createSmsMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sms_sent_to) + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    private String getMaskedPhone(String phoneNumber) {
        String masked = String.valueOf(phoneNumber).replaceFirst("(\\d{4})(\\d{4})(\\d+)",
                "$1-$2-$3");
        return String.format(
                ("<b>%s</b>"), masked);
    }

    private String createCallMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sent_to_call)
                    + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    @Override
    public String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getRegisterPhoneNumberVerificationIntent(Context context, String phoneNumber,
                                                            ArrayList<MethodItem> listAvailableMethod) {
        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putInt(PARAM_FRAGMENT_TYPE, TYPE_SMS);
        bundle.putParcelableArrayList(PARAM_METHOD_LIST, listAvailableMethod);
        intent.putExtras(bundle);
        return intent;
    }

    public void goToSelectVerificationMethod() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                VerificationMethodFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = VerificationMethodFragment.createInstance(getIntent().getExtras());
            fragmentTransaction.setCustomAnimations(com.tokopedia.core.R.animator.slide_in_left, 0, 0, com
                    .tokopedia.core.R.animator.slide_out_right);
            fragmentTransaction.add(R.id.container, fragment, CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    public void goToSmsVerification() {
        if (getIntent().getExtras() != null
                && (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                VerificationFragment))) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String phoneNumber = getIntent().getExtras().getString(PARAM_PHONE_NUMBER, "");

            Fragment fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(com.tokopedia.core.R.animator.slide_in_left, 0, 0,
                    com.tokopedia.core.R.animator.slide_out_right);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    public void goToCallVerification() {
        if (getIntent().getExtras() != null
                && (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                VerificationFragment))) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            String phoneNumber = getIntent().getExtras().getString(PARAM_PHONE_NUMBER, "");

            Fragment fragment = VerificationFragment.createInstance(createCallBundle(phoneNumber));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(com.tokopedia.core.R.animator.slide_in_left, 0, 0,
                    com.tokopedia.core.R.animator.slide_out_right);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    private Bundle createSmsBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_FRAGMENT_TYPE, TYPE_SMS);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_sms);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createSmsMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_SMS);
        return bundle;
    }

    private Bundle createCallBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_FRAGMENT_TYPE, TYPE_PHONE_CALL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_call);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createCallMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_CALL);
        return bundle;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
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
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
