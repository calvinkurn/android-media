package com.tokopedia.session.register.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.core.TermPrivacy;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customView.PasswordView;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.presenter.CreatePasswordPresenter;
import com.tokopedia.session.register.view.util.RegisterUtil;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 10/12/17.
 */

public class CreatePasswordFragment extends BaseDaggerFragment
        implements CreatePassword.View {

    private static final String CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED";
    private static final int REQUEST_VERIFY_PHONE_NUMBER = 101;
    EditText vName;
    TextView vBDay;
    PasswordView vPassword;
    PasswordView vPasswordRetype;
    EditText vPhoneNumber;
    View vSendButton;
    CheckBox termsOfServiceCheckBox;
    View vError;
    TextView termAndCond;
    TextView privacy;

    TkpdProgressDialog mProgressDialog;
    DatePickerDialog datePicker;
    DatePicker dp;

    @Inject
    CreatePasswordPresenter presenter;

    CreatePasswordViewModel model;

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            vBDay.setText(RegisterUtil.formatDateText(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            model.setBdayYear(year);
            model.setBdayMonth(monthOfYear);
            model.setBdayDay(dayOfMonth);
        }
    };

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CreatePasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_CREATE_PASSWORD;
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
        TrackingUtils.fragmentBasedAFEvent
                (RegisterAnalytics.Screen.IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            model = savedInstanceState.getParcelable(CreatePasswordActivity
                    .ARGS_FORM_DATA);
        } else if (getArguments() != null) {
            model = getArguments().getParcelable(CreatePasswordActivity
                    .ARGS_FORM_DATA);
        } else {
            getActivity().finish();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_password, parent, false);

        vName = (EditText) view.findViewById(R.id.user_name);
        vBDay = (TextView) view.findViewById(R.id.date);
        vPassword = (PasswordView) view.findViewById(R.id.password);
        vPasswordRetype = (PasswordView) view.findViewById(R.id.password_retype);
        vPhoneNumber = (EditText) view.findViewById(R.id.phone_number);
        vSendButton = view.findViewById(R.id.send_button);
        termsOfServiceCheckBox = (CheckBox) view.findViewById(R.id.tos_check);
        vError = view.findViewById(R.id.error);
        termAndCond = (TextView) view.findViewById(R.id.tos_tos);
        privacy = (TextView) view.findViewById(R.id.tos_privacy);

        prepareView(view);
        setViewListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
    }

    private void setData() {
        if (!TextUtils.isEmpty(model.getFullName())) {
            vName.setText(model.getFullName().toUpperCase().contains(CHARACTER_NOT_ALLOWED) ? "" : model.getFullName());
        }

        if (model.getBdayDay() != 0
                && model.getBdayMonth() != 0
                && model.getBdayYear() != 0
                && String.valueOf(model.getBdayYear()).length() == 4) {
            String birthDay = model.getBdayDay() + " / " +
                    model.getBdayMonth() + " / " + model.getBdayYear();
            vBDay.setText(birthDay);
        } else {
            vBDay.setText(R.string.day_month_year);
        }

    }

    private void setViewListener() {
        vName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setFullName(vName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setNewPass(vPassword.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vPasswordRetype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setConfirmPass(vPasswordRetype.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        vPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setMsisdn(vPhoneNumber.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        termsOfServiceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setRegisterTos(isChecked ? "1" : "0");
            }
        });
        termAndCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermPrivacy.start(getActivity(), TermPrivacy.T_AND_C);
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermPrivacy.start(getActivity(), TermPrivacy.P_P);
            }
        });
        vBDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = model.getBdayYear();
                int month = model.getBdayMonth();
                int day = model.getBdayDay();

                if (year == 0) year = 2002;
                DatePickerUtil datePicker = new DatePickerUtil(getActivity(), day, month, year);
                datePicker.SetMaxYear(2002);
                datePicker.SetMinYear(1934);
                datePicker.SetShowToday(false);
                datePicker.DatePickerCalendar(onDateSelected());

            }
        });
        vSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.createPassword(model);
            }
        });
    }

    private DatePickerUtil.onDateSelectedListener onDateSelected() {
        return new DatePickerUtil.onDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int dayOfMonth) {
                model.setBdayYear(year);
                model.setBdayMonth(month);
                model.setBdayDay(dayOfMonth);

                String birthday = RegisterUtil.formatDateText(
                        model.getBdayDay(),
                        model.getBdayMonth(),
                        model.getBdayYear());

                if (birthday.equals("1 / 1 / 0")) {
                    vBDay.setText(R.string.default_datepicker);
                } else {
                    vBDay.setText(birthday);
                }
            }
        };
    }

    private void prepareView(View view) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CreatePasswordActivity.ARGS_FORM_DATA, model);
    }

    @Override
    public void resetError() {
        vName.setError(null);
        vPassword.setError(null);
        vPhoneNumber.setError(null);
        vPasswordRetype.setError(null);
        vBDay.setError(null);
        vError.setVisibility(View.GONE);
    }

    @Override
    public void showErrorName(int resId) {
        vName.setError(getText(resId));
        vName.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.FULLNAME);
    }

    @Override
    public void showErrorPassword(int resId) {
        vPassword.setError(getText(resId));
        vPassword.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.PASSWORD);

    }

    @Override
    public void showErrorConfirmPassword(int resId) {
        vPasswordRetype.setError(getText(resId));
        vPasswordRetype.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.PASSWORD_CONFIRMATION);

    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        vPhoneNumber.setError(getText(resId));
        vPhoneNumber.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.HANDPHONE);
    }

    @Override
    public void showErrorBday(int resId) {
        vBDay.setError(getText(resId));
        vBDay.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.BIRTHDATE);

    }

    @Override
    public void showErrorTOS() {
        vError.setVisibility(View.VISIBLE);
        vError.requestFocus();
        UnifyTracking.eventRegisterError(AppEventTracking.EventLabel.TOS);
    }

    @Override
    public void onSuccessCreatePassword() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorCreatePassword(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onGoToPhoneVerification() {
        Intent intent = PhoneVerificationActivationActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_VERIFY_PHONE_NUMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFY_PHONE_NUMBER) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
