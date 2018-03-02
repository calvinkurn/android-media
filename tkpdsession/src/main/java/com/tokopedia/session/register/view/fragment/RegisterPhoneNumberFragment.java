package com.tokopedia.session.register.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;
import com.tokopedia.session.register.view.presenter.RegisterPhoneNumberPresenter;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberFragment extends BaseDaggerFragment
        implements RegisterPhoneNumber.View {

    private static final int REQUEST_VERIFY_PHONE = 101;
    private static final int REQUEST_CHOOSE_ACCOUNT = 102;
    private static final int REQUEST_PHONE_NOT_CONNECTED = 103;
    private static final int REQUEST_NO_TOKOCASH_ACCOUNT = 104;

    EditText phoneNumber;
    TextView nextButton;
    TextView message;
    TextView errorText;
    TextView bottomInfo;

    TkpdProgressDialog progressDialog;

    @Inject
    RegisterPhoneNumberPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new RegisterPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
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
        View view = inflater.inflate(R.layout.fragment_register_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        errorText = view.findViewById(R.id.error);
        bottomInfo = view.findViewById(R.id.botton_info);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidNumber(charSequence.toString())) {
                    enableButton(nextButton);
                } else  {
                    disableButton(nextButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (isValidNumber(v.getText().toString())) {
//                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                        presenter.registerWithPhoneNumber(phoneNumber.getText().toString());
                    }
                    handled = true;
                }
                return handled;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                presenter.registerWithPhoneNumber(phoneNumber.getText().toString());
            }
        });

        bottomInfo.setLinkTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
    }

    public boolean isValidNumber(String phoneNumber) {
        if (phoneNumber.length() == 0) {
            errorText.setText(getResources().getString(R.string.error_input_phone_number));
            return false;
        }
        if (phoneNumber.length() < 8) {
            errorText.setText(getResources().getString(R.string.error_char_count_under));
            return false;
        }
        errorText.setText("");
        return true;
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
        startActivityForResult(VerificationActivity.getRegisterPhoneNumberVerificationIntent(
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
    public void showAlreadyRegisteredDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(phoneNumber));
        builder.setMessage(getResources().getString(R.string.phone_number_already_registered));
        builder.setPositiveButton(getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
            //go to login
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void showConfirmationPhoneNumber(String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(phoneNumber));
        builder.setMessage(getResources().getString(R.string.phone_number_not_registered));
        builder.setPositiveButton(getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //go to login
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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

    private void enableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(true);
    }

}
