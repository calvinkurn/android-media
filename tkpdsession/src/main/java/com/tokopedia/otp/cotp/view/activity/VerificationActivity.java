package com.tokopedia.otp.cotp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.session.R;

/**
 * @author by nisie on 11/29/17.
 *         <p>
 *         Please build VerificationPassModel and put it to cachemanager with key PASS_MODEL.
 *         This is to prevent TransactionTooLarge.
 */

public class VerificationActivity extends TActivity implements HasComponent {

    public static final String PASS_MODEL = "VerificationPassModel";

    public static final int TYPE_SMS = 1;
    public static final int TYPE_PHONE_CALL = 2;
    public static final int TYPE_EMAIL = 3;

    public static final String PARAM_DEFAULT_FRAGMENT_TYPE = "fragmentType";
    public static final String PARAM_OTP_TYPE = "otpType";

    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_APP_SCREEN = "app_screen";
    public static final String PARAM_MESSAGE = "message";

    private static final String FIRST_FRAGMENT_TAG = "first";
    private static final String CHOOSE_FRAGMENT_TAG = "choose";

    private VerificationPassModel passModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {

        setupPassdata();

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        Fragment fragment;
        fragment = getFragment(getIntent().getExtras().getInt(PARAM_DEFAULT_FRAGMENT_TYPE, -1), bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    private void setupPassdata() {
        if (globalCacheManager != null
                && globalCacheManager.getConvertObjData(PASS_MODEL, VerificationPassModel.class) != null) {
            passModel = globalCacheManager.getConvertObjData(PASS_MODEL, VerificationPassModel.class);
        } else {
            Log.d(VerificationActivity.class.getSimpleName(), "Error no pass data");
            finish();
        }
    }

    private Fragment getFragment(int type, Bundle bundle) {
        Fragment fragment;
        switch (type) {
            case TYPE_SMS: {
                String phoneNumber = passModel.getPhoneNumber();
                fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber));
                break;
            }
            case TYPE_EMAIL: {
                String email = passModel.getEmail();
                fragment = VerificationFragment.createInstance(createEmailBundle(email));
                break;
            }
            default: {
                fragment = ChooseVerificationMethodFragment.createInstance(bundle);
                break;
            }
        }
        return fragment;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_COTP_DEFAULT;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }

    public static Intent getCallingIntent(Context context, int defaultType) {
        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_DEFAULT_FRAGMENT_TYPE, defaultType);
        intent.putExtras(bundle);
        return intent;
    }

    public void goToSelectVerificationMethod() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                ChooseVerificationMethodFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = ChooseVerificationMethodFragment.createInstance(getIntent().getExtras());
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
            String phoneNumber = passModel.getPhoneNumber();

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
            String phoneNumber = passModel.getPhoneNumber();

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
        bundle.putInt(PARAM_DEFAULT_FRAGMENT_TYPE, TYPE_SMS);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_sms);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createSmsMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, AppScreen.SCREEN_COTP_SMS);
        return bundle;
    }

    private Bundle createCallBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_DEFAULT_FRAGMENT_TYPE, TYPE_PHONE_CALL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_call);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createCallMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, AppScreen.SCREEN_COTP_CALL);
        return bundle;
    }

    private Bundle createEmailBundle(String email) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_DEFAULT_FRAGMENT_TYPE, TYPE_EMAIL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_email);
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_MESSAGE, createEmailMessage(email));
        bundle.putString(PARAM_APP_SCREEN, AppScreen.SCREEN_COTP_EMAIL);
        return bundle;
    }

    private String getMaskedPhone(String phoneNumber) {
        String masked = String.valueOf(phoneNumber).replaceFirst("(\\d{4})(\\d{4})(\\d+)",
                "$1-$2-$3");
        return String.format(
                ("<b>%s</b>"), masked);
    }

    private String createSmsMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sent_to) + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    private String createCallMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sent_to_call)
                    + " " + getMaskedPhone(phoneNumber);
        } else {
            return "";
        }
    }

    private String createEmailMessage(String email) {
        if (!TextUtils.isEmpty(email)) {
            return getString(R.string.verification_code_sent_to)
                    + " <b>" + email + "</b>";
        } else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
