package com.tokopedia.otp.centralizedotp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.otp.centralizedotp.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.centralizedotp.fragment.VerificationFragment;
import com.tokopedia.otp.centralizedotp.viewmodel.MethodItem;
import com.tokopedia.session.R;

import java.util.ArrayList;

/**
 * @author by nisie on 11/29/17.
 */

public class VerificationActivity extends TActivity implements HasComponent {

    public static final int TYPE_SMS = 1;
    public static final int TYPE_PHONE_CALL = 2;

    public static final String PARAM_FRAGMENT_TYPE = "type";
    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_MESSAGE = "message";
    public static final String PARAM_APP_SCREEN = "app_screen";
    public static final String PARAM_METHOD_LIST = "method_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_simple_fragment);
        initView();
    }

    private void initView() {
        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        Fragment fragment;
        fragment = getFragment(getIntent().getExtras().getInt(PARAM_FRAGMENT_TYPE, -1), bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

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
                fragment = ChooseVerificationMethodFragment.createInstance(bundle);
                break;
            }
        }
        return fragment;
    }

    private String createSmsMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.substring(phoneNumber.length() - 4);
            String maskedPhone = String.format(
                    ("<b>****-****- %s </b>"), phoneNumber);
            return getString(R.string.verification_code_sent_to_sms) + " " + maskedPhone;
        } else {
            return "";
        }
    }

    private String createCallMessage(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.substring(phoneNumber.length() - 4);
            String maskedPhone = String.format(
                    ("<b>****-****- %s </b>"), phoneNumber);
            return getString(R.string.verification_code_sent_to_call) + " " + maskedPhone;
        } else {
            return "";
        }
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_LOGIN_PHONE_NUMBER;
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }


    public static Intent getSmsVerificationIntent(Context context, String phoneNumber,
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
        Fragment fragment = ChooseVerificationMethodFragment.createInstance(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    public void goToSmsVerification() {
        if (getIntent().getExtras() != null) {
            String phoneNumber = getIntent().getExtras().getString(PARAM_PHONE_NUMBER, "");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(VerificationFragment.class
                    .getSimpleName());
            if (fragment == null)
                fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber));
            else
                ((VerificationFragment) fragment).setData(createSmsBundle(phoneNumber));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    public void goToCallVerification() {
        if (getIntent().getExtras() != null) {
            String phoneNumber = getIntent().getExtras().getString(PARAM_PHONE_NUMBER, "");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(VerificationFragment.class
                    .getSimpleName());
            if (fragment == null)
                fragment = VerificationFragment.createInstance(createCallBundle(phoneNumber));
            else
                ((VerificationFragment) fragment).setData(createSmsBundle(phoneNumber));
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
    }

    private Bundle createSmsBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_FRAGMENT_TYPE, TYPE_SMS);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_sms);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createSmsMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, AppScreen.SCREEN_COTP_SMS);
        return bundle;
    }

    private Bundle createCallBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_FRAGMENT_TYPE, TYPE_PHONE_CALL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_call);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createCallMessage(phoneNumber));
        bundle.putString(PARAM_APP_SCREEN, AppScreen.SCREEN_COTP_CALL);
        return bundle;
    }

}
