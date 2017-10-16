package com.tokopedia.session.register.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.TermPrivacy;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customView.PasswordView;
import com.tokopedia.session.R;
import com.tokopedia.session.di.DaggerSessionComponent;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.presenter.CreatePasswordPresenter;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;
import com.tokopedia.session.session.presenter.RegisterNewImpl;

import javax.inject.Inject;

/**
 * @author by nisie on 10/12/17.
 */

public class CreatePasswordFragment extends BaseDaggerFragment
        implements CreatePassword.View {

    EditText vName;
    TextView vBDay;
    PasswordView vPassword;
    PasswordView vPasswordRetype;
    EditText vPhoneNumber;
    View vSendButton;
    CheckBox vTos;
    View vError;
    TextView termAndCond;
    TextView privacy;

    TkpdProgressDialog mProgressDialog;
    DatePickerDialog datePicker;
    DatePicker dp;

    @Inject
    CreatePasswordPresenter presenter;

    CreatePasswordModel model;

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            vBDay.setText(RegisterNewImpl.RegisterUtil.formatDateText(
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
        return AppScreen.SCREEN_CREATE_PASSWORD;
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
        vTos = (CheckBox) view.findViewById(R.id.tos_check);
        vError = view.findViewById(R.id.error);
        termAndCond = (TextView) view.findViewById(R.id.tos_tos);
        privacy = (TextView) view.findViewById(R.id.tos_privacy);

        prepareView(view);
        setViewListener();
        presenter.attachView(this);
        return view;
    }

    private void setViewListener() {
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

                String birthday = RegisterNewImpl.RegisterUtil.formatDateText(
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
}
