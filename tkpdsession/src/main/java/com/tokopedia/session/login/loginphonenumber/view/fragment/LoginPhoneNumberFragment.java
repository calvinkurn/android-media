package com.tokopedia.session.login.loginphonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.session.login.loginphonenumber.view.presenter.LoginPhoneNumberPresenter;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.LoginPhoneNumber;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberFragment extends BaseDaggerFragment
        implements LoginPhoneNumber.View {

    private static final int REQUEST_VERIFY_PHONE = 101;
    private static final int REQUEST_CHOOSE_ACCOUNT = 102;
    private static final int REQUEST_PHONE_NOT_CONNECTED = 103;
    private static final int REQUEST_NO_TOKOCASH_ACCOUNT = 104;

    EditText phoneNumber;
    TextView nextButton;
    TextView message;
    TextView errorText;

    @Inject
    LoginPhoneNumberPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new LoginPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_LOGIN_PHONE_NUMBER;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerSessionComponent.inject(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            model = savedInstanceState.getParcelable(LoginPhoneNumberActivity
//                    .ARGS_FORM_DATA);
//        } else if (getArguments() != null) {
//            model = getArguments().getParcelable(LoginPhoneNumberActivity
//                    .ARGS_FORM_DATA);
//        } else {
//            getActivity().finish();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        errorText = view.findViewById(R.id.error);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        showErrorPhoneNumber(getString(resId));
    }

    @Override
    public void goToVerifyAccountPage(String phoneNumber) {
        startActivityForResult(VerificationActivity.getSmsVerificationIntent(
                getActivity(),
                phoneNumber,
                getListVerificationMethod()),
                REQUEST_VERIFY_PHONE);
    }

    @Override
    public void goToNoTokocashAccountPage() {
        startActivityForResult(NotConnectedTokocashActivity.getNoTokocashAccountIntent(
                getActivity(),
                phoneNumber.getText().toString()),
                REQUEST_NO_TOKOCASH_ACCOUNT);
    }

    private ArrayList<MethodItem> getListVerificationMethod() {
        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                VerificationActivity.TYPE_SMS,
                R.drawable.ic_verification_sms,
                MethodItem.getSmsMethodText(phoneNumber.getText().toString())
        ));
        list.add(new MethodItem(
                VerificationActivity.TYPE_PHONE_CALL,
                R.drawable.ic_verification_call,
                MethodItem.getCallMethodText(phoneNumber.getText().toString())
        ));
        return list;
    }

    private void showErrorPhoneNumber(String errorMessage) {
        errorText.setText(errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFY_PHONE
                && resultCode == Activity.RESULT_OK) {
            goToChooseAccountPage(data);
        } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void goToChooseAccountPage(Intent data) {
        startActivityForResult(ChooseTokocashAccountActivity.getCallingIntent(
                getActivity(),
                getChooseAccountData(data)),
                REQUEST_CHOOSE_ACCOUNT);
    }

    private ChooseTokoCashAccountViewModel getChooseAccountData(Intent data) {
        return data.getParcelableExtra(ChooseTokocashAccountActivity.ARGS_DATA);
    }
}
