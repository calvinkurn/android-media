package com.tokopedia.session.login.loginphonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginemail.view.activity.ForbiddenActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
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
    TextView changeInactiveNumber;
    String phoneNumberString;

    TkpdProgressDialog progressDialog;

    @Inject
    LoginPhoneNumberPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new LoginPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_LOGIN_PHONE_NUMBER;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        errorText = view.findViewById(R.id.error);
        changeInactiveNumber = view.findViewById(R.id.change_inactive);
        presenter.attachView(this);
        prepareView();
        return view;
    }

    private void prepareView() {
        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                    presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorText.setText("");
                UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                presenter.loginWithPhoneNumber(phoneNumber.getText().toString());
            }
        });

        if (getArguments() != null) {
            if (getArguments().get(LoginPhoneNumberActivity.PARAM_PHONE_NUMBER) != null) {
                phoneNumberString = getArguments().getString(LoginPhoneNumberActivity.PARAM_PHONE_NUMBER);
                phoneNumber.setText(phoneNumberString);
                nextButton.performClick();
            }
        }

        SpannableString changeInactiveString = new SpannableString(changeInactiveNumber.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = SimpleWebViewWithFilePickerActivity.
                        getIntentWithTitle(
                                getContext(),
                                SessionUrl.ChangePhone.PATH_WEBVIEW_CHANGE_PHONE_NUMBER,
                                "Ubah Nomor Ponsel");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                ds.setUnderlineText(false);
            }
        };

        changeInactiveString.setSpan(clickableSpan, 37, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        changeInactiveNumber.setText(changeInactiveString);
        changeInactiveNumber.setMovementMethod(LinkMovementMethod.getInstance());
        changeInactiveNumber.setHighlightColor(Color.TRANSPARENT);
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
        startActivityForResult(VerificationActivity.getLoginTokoCashVerificationIntent(
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

    @Override
    public void dismissLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        progressDialog.showDialog();
    }

    @Override
    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
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

    @Override
    public void showErrorPhoneNumber(String errorMessage) {
        errorText.setText(errorMessage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFY_PHONE
                && resultCode == Activity.RESULT_OK) {
            ChooseTokoCashAccountViewModel chooseTokoCashAccountViewModel = getChooseAccountData(data);
            if (chooseTokoCashAccountViewModel != null && !chooseTokoCashAccountViewModel
                    .getListAccount().isEmpty()) {
                goToChooseAccountPage(chooseTokoCashAccountViewModel);
            } else {
                goToNoTokocashAccountPage();
            }
        } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void goToChooseAccountPage(ChooseTokoCashAccountViewModel data) {
        startActivityForResult(ChooseTokocashAccountActivity.getCallingIntent(
                getActivity(),
                data),
                REQUEST_CHOOSE_ACCOUNT);
    }

    private ChooseTokoCashAccountViewModel getChooseAccountData(Intent data) {
        return data.getParcelableExtra(ChooseTokocashAccountActivity.ARGS_DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
