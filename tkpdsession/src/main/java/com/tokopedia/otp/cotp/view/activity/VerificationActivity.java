package com.tokopedia.otp.cotp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.analytics.OTPAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.InterruptVerificationFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginemail.view.fragment.LoginFragment;

/**
 * @author by nisie on 11/29/17.
 *         <p>
 *         Please build VerificationPassModel and put it to cachemanager with key PASS_MODEL.
 *         This is to prevent TransactionTooLarge.
 */

public class VerificationActivity extends TActivity implements HasComponent {

    public static final String PASS_MODEL = "VerificationPassModel";

    public static final String PARAM_REQUEST_OTP_MODE = "fragmentType";

    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_IMAGE_URL = "image_url";
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (passModel.getInterruptModel() != null) {
            fragment = InterruptVerificationFragment.createInstance(bundle);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        } else {
            fragment =
                    getDefaultFragment(getIntent().getExtras().getString(PARAM_REQUEST_OTP_MODE, ""), bundle);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        }

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

    private Fragment getDefaultFragment(String mode, Bundle bundle) {
        Fragment fragment;
        switch (mode) {
            case RequestOtpUseCase.MODE_SMS: {
                String phoneNumber = passModel.getPhoneNumber();
                int otpType = passModel.getOtpType();
                fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber, otpType));
                break;
            }
            case RequestOtpUseCase.MODE_EMAIL: {
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

    /**
     * @param mode should be from {@link com.tokopedia.otp.domain.interactor.RequestOtpUseCase}
     *             Do not use this for dynamic verification page. This should only be used for default page.
     */
    public void goToDefaultVerificationPage(String mode) {
        if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                VerificationFragment)) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = getDefaultFragment(mode, getIntent().getExtras());
            fragmentTransaction.setCustomAnimations(com.tokopedia.core.R.animator.slide_in_left, 0, 0, com
                    .tokopedia.core.R.animator.slide_out_right);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    /**
     * @param methodItem should be from {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     *                   Use this for dynamic otp.
     */
    public void goToVerificationPage(MethodItem methodItem) {
        if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof
                VerificationFragment)) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = VerificationFragment.createInstance(createDynamicBundle(methodItem));
            fragmentTransaction.setCustomAnimations(com.tokopedia.core.R.animator.slide_in_left, 0, 0, com
                    .tokopedia.core.R.animator.slide_out_right);
            fragmentTransaction.add(R.id.container, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    private Bundle createSmsBundle(String phoneNumber, int otpType) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_sms);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createSmsMessage(phoneNumber, otpType));
        bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_SMS);
        return bundle;
    }

    private Bundle createEmailBundle(String email) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_EMAIL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_email);
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_MESSAGE, createEmailMessage(email));
        bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_EMAIL);
        return bundle;
    }

    private Bundle createDynamicBundle(MethodItem methodItem) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, methodItem.getModeName());
        bundle.putInt(PARAM_IMAGE, 0);
        bundle.putString(PARAM_IMAGE_URL, methodItem.getImageUrl());
        bundle.putString(PARAM_PHONE_NUMBER, passModel.getPhoneNumber());
        bundle.putString(PARAM_EMAIL, passModel.getEmail());
        bundle.putString(PARAM_MESSAGE, methodItem.getVerificationText());
        bundle.putString(PARAM_APP_SCREEN, getDynamicAppScreen(methodItem.getModeName()));
        return bundle;
    }

    private String getDynamicAppScreen(String mode) {
        return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT + mode;
    }

    private String getMaskedPhone(String phoneNumber, int otpType) {
        if (otpType == RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION) {
            return MethodItem.getMaskedPhoneNumber(phoneNumber);
        } else {
            String masked = String.valueOf(phoneNumber).replaceFirst("(\\d{4})(\\d{4})(\\d+)",
                    "$1-$2-$3");
            return String.format(
                    ("<b>%s</b>"), masked);
        }
    }

    private String createSmsMessage(String phoneNumber, int otpType) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sms_sent_to) + "<br/>" + getMaskedPhone
                    (phoneNumber, otpType);
        } else {
            return "";
        }
    }

    private String createEmailMessage(String email) {
        if (!TextUtils.isEmpty(email)) {
            return getString(R.string.verification_code_email_sent_to)
                    + "<br/><b>" + email + "</b>";
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


    public static Intent getSecurityQuestionVerificationIntent(Context context, int
            typeSecurityQuestion) {
        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        if (typeSecurityQuestion == LoginFragment.TYPE_SQ_PHONE) {
            bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        } else {
            bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_EMAIL);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallingIntent(Context context, String requestMode) {
        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, requestMode);
        intent.putExtras(bundle);
        return intent;
    }
}
