package com.tokopedia.session.register.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.MaterialSpinner;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.activity.ActivationActivity;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.interactor.RegisterNetworkInteractorImpl;
import com.tokopedia.session.register.model.RegisterStep1ViewModel;
import com.tokopedia.session.register.model.RegisterViewModel;
import com.tokopedia.session.register.model.gson.RegisterResult;
import com.tokopedia.session.register.presenter.RegisterStep2Presenter;
import com.tokopedia.session.register.presenter.RegisterStep2PresenterImpl;
import com.tokopedia.session.register.util.RegisterUtil;
import com.tokopedia.session.register.viewlistener.RegisterStep2ViewListener;
import com.tokopedia.session.session.presenter.RegisterNewImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterStep2Fragment extends BasePresenterFragment<RegisterStep2Presenter>
        implements RegisterStep2ViewListener, RegisterConstant {

    @BindView(R2.id.register_next_status)
    LinearLayout registerNextStatus;
    @BindView(R2.id.register_next_status_message)
    TextView registerNextStatusMessage;
    @BindView(R2.id.register_next_step_2)
    LinearLayout registerNextStep2;
    @BindView(R2.id.register_next_full_name)
    TextView registerNextFullName;
    @BindView(R2.id.register_next_phone_number)
    EditText registerNextPhoneNumber;
    @BindView(R2.id.register_finish_button)
    TextView registerFinish;
    @BindView(R2.id.register_next_detail_t_and_p)
    TextView registerNextTAndC;
    @BindView(R2.id.wrapper_phone)
    TextInputLayout wrapperPhone;
    @BindView(R2.id.wrapper_gender)
    TextInputLayout wrapperGender;
    @BindView(R2.id.spinner)
    MaterialSpinner spinnerGender;
    @BindView(R2.id.wrapper_date)
    TextInputLayout wrapperDate;
    @BindView(R2.id.date)
    MaterialSpinner dateText;

    String name;
    TkpdProgressDialog progressDialog;
    DatePickerDialog datePicker;
    DatePicker dp;

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateText.setText(RegisterNewImpl.RegisterUtil.formatDateTextString(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            presenter.getViewModel().setDateYear(year);
            presenter.getViewModel().setDateMonth(monthOfYear);
            presenter.getViewModel().setDateDay(dayOfMonth);
        }
    };

    public static RegisterStep2Fragment createInstance(RegisterStep1ViewModel model) {
        RegisterStep2Fragment fragment = new RegisterStep2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, model.getName());
        bundle.putString(EMAIL, model.getEmail());
        bundle.putString(PASSWORD, model.getPassword());
        bundle.putBoolean(IS_AUTO_VERIFY, model.isAutoVerify());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        presenter = new RegisterStep2PresenterImpl(this,
                new RegisterNetworkInteractorImpl(new AccountsService(bundle)),
                new CompositeSubscription());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register_step2;
    }

    @Override
    protected void initView(View view) {
        name = getArguments().getString(NAME);
        registerNextFullName.setText("Halo, " + name + "!");
        registerFinish.setBackgroundResource(com.tokopedia.core.R.drawable.bg_rounded_corners);

        if (presenter.getViewModel().getMinDate() == 0) {
            presenter.calculateDateTime();
        }

        datePicker = new DatePickerDialog(getActivity(),
                callBack,
                presenter.getViewModel().getDateYear(),
                presenter.getViewModel().getDateMonth(),
                presenter.getViewModel().getDateDay());

        dp = datePicker.getDatePicker();
        dp.setMaxDate(presenter.getViewModel().getMaxDate());
        dp.setMinDate(presenter.getViewModel().getMinDate());

        dateText.setText(RegisterNewImpl.RegisterUtil.formatDateTextString(1, 1, 1989));
        showTermsAndOptionsTextView();
    }

    private void showTermsAndOptionsTextView() {
        String joinString = context.getString(com.tokopedia.core.R.string.detail_term_and_privacy) +
                " " + context.getString(com.tokopedia.core.R.string.link_term_condition) +
                ", serta " + context.getString(com.tokopedia.core.R.string.link_privacy_policy) + " Tokopedia";

        registerNextTAndC.setText(MethodChecker.fromHtml(joinString));
        registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onResume() {
        super.onResume();
        registerNextPhoneNumber.addTextChangedListener(watcher(wrapperPhone));
        registerNextPhoneNumber.addTextChangedListener(watcher(registerNextPhoneNumber));
    }

    private TextWatcher watcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = CustomPhoneNumberUtil.transform(s.toString());
                if (s.toString().length() != phone.length()) {
                    editText.removeTextChangedListener(this);
                    editText.setText(phone);
                    editText.setSelection(phone.length());
                    editText.addTextChangedListener(this);
                }
            }
        };
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(com.tokopedia.core.R.string.error_field_required));
                }
            }
        };
    }


    @Override
    protected void setViewListener() {
        spinnerGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(spinnerGender.getContext(), spinnerGender);
                popup.getMenuInflater().inflate(com.tokopedia.core.R.menu.gender_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        spinnerGender.setText(item.getTitle());
                        setWrapperError(wrapperGender, null);
                        if (item.getTitle().equals("Pria")) {
                            presenter.getViewModel().setGender(RegisterViewModel.GENDER_MALE);
                        } else if (item.getTitle().equals("Wanita")) {
                            presenter.getViewModel().setGender(RegisterViewModel.GENDER_FEMALE);
                        }
                        KeyboardHandler.DropKeyboard(getActivity(), getView());
                        return true;
                    }
                });

                popup.show();
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerUtil datePicker = new DatePickerUtil(getActivity(), 1, 1, 1989);
                datePicker.SetMaxYear(2002);
                datePicker.SetMinYear(1934);
                datePicker.SetShowToday(false);
                datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int month, int dayOfMonth) {
                        presenter.getViewModel().setDateYear(year);
                        presenter.getViewModel().setDateMonth(month);
                        presenter.getViewModel().setDateDay(dayOfMonth);
                        presenter.getViewModel().setDateText(
                                RegisterUtil.formatDateTextString(dayOfMonth,
                                        month, year));

                        dateText.setText(presenter.getViewModel().getDateText());
                    }
                });
            }
        });

        registerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.finishRegister();
            }
        });

    }

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }


    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS);
        }

        if (getActivity() != null) {
            progressDialog.showDialog();
        }
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void resetError() {
        setWrapperError(wrapperPhone, null);
        setWrapperError(wrapperGender, null);
        setWrapperError(wrapperDate, null);
    }

    @Override
    public EditText getPhone() {
        return registerNextPhoneNumber;
    }

    @Override
    public void setPhoneError(String errorMessage) {
        finishLoadingProgress();
        setWrapperError(wrapperPhone, errorMessage);
        registerNextPhoneNumber.requestFocus();

    }

    @Override
    public void dropKeyboard() {
        View view = getActivity().getCurrentFocus();
        KeyboardHandler.DropKeyboard(getActivity(), view);
    }

    @Override
    public MaterialSpinner getGender() {
        return spinnerGender;
    }

    @Override
    public void setGenderError(String errorMessage) {
        finishLoadingProgress();
        setWrapperError(wrapperGender, errorMessage);
        spinnerGender.requestFocus();
    }

    @Override
    public void onErrorRegister(String errorMessage) {
        finishLoadingProgress();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRegister(RegisterResult registerResult) {
        finishLoadingProgress();

        if (registerResult.getIsActive() == RegisterResult.USER_INACTIVE
                || registerResult.getIsActive() == RegisterResult.USER_PENDING ) {
            sendLocalyticsRegisterEvent(registerResult.getUserId());
            sendGTMRegisterEvent();
            goToRegisterActivation();
        } else {
            onErrorRegister(getString(R.string.alert_email_address_is_already_registered));
        }


    }

    private void goToRegisterActivation() {
        startActivity(ActivationActivity.getCallingIntent(
                getActivity(),
                presenter.getViewModel().getRegisterStep1ViewModel().getEmail(),
                presenter.getViewModel().getRegisterStep1ViewModel().getName()));
        getActivity().finish();
    }

    private void sendLocalyticsRegisterEvent(int userId) {
        Map<String, String> attributesLogin = new HashMap<String, String>();
        CustomerWrapper customerLogin = new CustomerWrapper();
        customerLogin.setCustomerId(Integer.toString(userId));
        customerLogin.setFullName(presenter.getViewModel().getRegisterStep1ViewModel().getName());
        customerLogin.setEmailAddress(presenter.getViewModel().getRegisterStep1ViewModel().getEmail());
        customerLogin.setExtraAttr(attributesLogin);
        customerLogin.setMethod(getString(com.tokopedia.core.R.string.title_email));
        UnifyTracking.eventLoginLoca(customerLogin);
    }

    private void sendGTMRegisterEvent() {
        UnifyTracking.eventRegisterSuccess(getString(com.tokopedia.core.R.string.title_email));
    }

    @Override
    public void setRegisterModel(RegisterViewModel registerViewModel) {
        RegisterStep1ViewModel step1ViewModel = new RegisterStep1ViewModel();
        step1ViewModel.setName(getArguments().getString(NAME));
        step1ViewModel.setEmail(getArguments().getString(EMAIL));
        step1ViewModel.setAutoVerify(getArguments().getBoolean(IS_AUTO_VERIFY));
        step1ViewModel.setPassword(getArguments().getString(PASSWORD));

        registerViewModel.setRegisterStep1ViewModel(step1ViewModel);
        registerViewModel.setPhone(registerNextPhoneNumber.getText().toString().replace("-", ""));
        registerViewModel.setAgreedTermCondition(true);
        registerViewModel.setConfirmPassword(step1ViewModel.getPassword());
    }
}
