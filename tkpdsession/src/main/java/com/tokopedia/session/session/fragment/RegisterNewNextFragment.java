package com.tokopedia.session.session.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.widget.MaterialSpinner;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.session.session.model.RegisterSuccessModel;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.session.session.presenter.LoginImpl;
import com.tokopedia.session.session.presenter.RegisterNewImpl;
import com.tokopedia.session.session.presenter.RegisterNewNext;
import com.tokopedia.session.session.presenter.RegisterNewNextImpl;
import com.tokopedia.session.session.presenter.RegisterNewNextView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 1/25/16.
 * modified by m.normansyah on 2/10/16 AN-1382
 */
public class RegisterNewNextFragment extends BaseFragment<RegisterNewNext> implements RegisterNewNextView {

    DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateText.setText(RegisterNewImpl.RegisterUtil.formatDateTextString(
                    dayOfMonth,
                    monthOfYear,
                    year
            ));
            presenter.updateData(RegisterNewNext.DATE_YEAR, year);
            presenter.updateData(RegisterNewNext.DATE_MONTH, monthOfYear);
            presenter.updateData(RegisterNewNext.DATE_DAY, dayOfMonth);
        }
    };

    DatePickerDialog datePicker;
    DatePicker dp;

    public static Fragment newInstance(String name, String email, String password, boolean isAutoVerify){
        RegisterNewNextFragment fragment = new RegisterNewNextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(NAME, name);
        bundle.putString(EMAIL, email);
        bundle.putString(PASSWORD, password);
        bundle.putBoolean(IS_AUTO_VERIFY, isAutoVerify);
        fragment.setArguments(bundle);
        return fragment;
    }

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
    MaterialSpinner spinner;
    @BindView(R2.id.wrapper_date)
    TextInputLayout wrapperDate;
    @BindView(R2.id.date)
    MaterialSpinner dateText;


    String name;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup =new PopupMenu(spinner.getContext(),spinner);
                popup.getMenuInflater().inflate(R.menu.gender_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        spinner.setText(item.getTitle());
                        setWrapperError(wrapperGender,null);
                        if(item.getTitle().equals("Pria")){
                            presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_MALE);
                        }else if(item.getTitle().equals("Wanita")){
                            presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_FEMALE);
                        }
                        KeyboardHandler.DropKeyboard(getActivity(),getView());
                        return true;
                    }
                });

                popup.show();
            }
        });

        name = getArguments().getString(NAME);
        registerNextFullName.setText("Halo, "+ name +"!");

        registerFinish.setBackgroundResource(R.drawable.bg_rounded_corners);
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
                if(s.toString().length() != phone.length()) {
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
                if (s.length() == 0){
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                }
            }
        };
    }

    @OnClick(R2.id.date)
    public void onDateTextClickNew(){
        //datePicker.show();
        DatePickerUtil datePicker = new DatePickerUtil(getActivity(), 1, 1, 1989);
        datePicker.SetMaxYear(2002);
        datePicker.SetMinYear(1934);
        datePicker.SetShowToday(false);
        datePicker.DatePickerCalendar((DatePickerUtil.onDateSelectedListener)presenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(name!=null){
            presenter.updateData(RegisterNewNext.FULLNAME, name);
        }
        if(registerNextPhoneNumber!=null){
            presenter.updateData(RegisterNewNext.PHONT, registerNextPhoneNumber.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.saveBeforeDestroy(getActivity(), name, registerNextPhoneNumber.getText().toString());
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterNewNextImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_4;
    }

    @OnClick(R2.id.register_finish_button)
    public void registerFinish(){
        String mPhone = registerNextPhoneNumber.getText().toString();
        mPhone = mPhone.replace("-","");
        String mBirthDay = dateText.getText().toString();

        View focusView = null;
        boolean cancel = false;

        setWrapperError(wrapperPhone, null);
        setWrapperError(wrapperGender, null);
        setWrapperError(wrapperDate, null);

        if(TextUtils.isEmpty(mPhone)){
            setWrapperError(wrapperPhone, getString(R.string.error_field_required));
            focusView = registerNextPhoneNumber;
            cancel = true;
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
        }else {
            boolean validatePhoneNumber = RegisterNewNextImpl.validatePhoneNumber(mPhone);
            Log.e(RegisterNewNextView.TAG, messageTAG + " valid nomornya : " + validatePhoneNumber);
            RegisterNewNextImpl.testPhoneNumberValidation();
            if(!validatePhoneNumber){
                setWrapperError(wrapperPhone, getString(R.string.error_invalid_phone_number));
                focusView = registerNextPhoneNumber;
                cancel = true;
                sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            }
        }

        if(spinner.getText().length()==0){
            setWrapperError(wrapperGender, getString(R.string.message_need_to_select_gender));
            sendGTMRegisterError(AppEventTracking.EventLabel.GENDER);
            cancel = true;
        }

        if (cancel) {
            if(focusView!=null)
                focusView.requestFocus();
        } else {
            View view = getActivity().getCurrentFocus();
            KeyboardHandler.DropKeyboard(getActivity(),view);
            RegisterViewModel registerViewModel = presenter.compileAll(name, mPhone);
            sendGTMClickStepTwo();
            presenter.register(getActivity(), registerViewModel);
        }

    }

//    @OnClick(R2.id.register_next_date)
//    public void onDateTextClick(){
//        //datePicker.show();
//
//        int day = (int) presenter.getData(RegisterNewNext.DATE_DAY);
//        int month = (int) presenter.getData(RegisterNewNext.DATE_MONTH);
//        int year = (int) presenter.getData(RegisterNewNext.DATE_YEAR);
//        if(year==0) year=2002;
//        DatePickerUtil datePicker = new DatePickerUtil(getActivity(), day, month, year);
//        datePicker.SetMaxYear(2002);
//        datePicker.SetMinYear(1934);
//        datePicker.SetShowToday(false);
//        datePicker.DatePickerCalendar((DatePickerUtil.onDateSelectedListener)presenter);
//    }

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if(s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        }else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    @Override
    public void initDatePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth) {
        datePicker = new DatePickerDialog(getActivity(), callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void initDatePicker(long maxtime, long mintime) {
        dp = datePicker.getDatePicker();
        dp.setMaxDate(maxtime);
        dp.setMinDate(mintime);
    }

    @Override
    public void setData(int type, Object... data) {
        switch (type){
            case FULLNAME:
                String text = (String)data[0];
                registerNextFullName.setText("Halo, "+ text +"!");
                break;
            case TELEPHONE:
                text = (String)data[0];
                registerNextPhoneNumber.setText(text);
                break;
            case T_AND_C:
                String joinString = (String)data[0];
                registerNextTAndC.setText(MethodChecker.fromHtml(joinString));
                registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case TTL:
                text = (String)data[0];
                dateText.setText(text);
                break;
            default:
                throw new RuntimeException(messageTAG+"please register type here!!!");
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    public void showProgress(final boolean show) {
        final WeakReference<LinearLayout> loginstatus = new WeakReference<>(registerNextStatus);
        final WeakReference<LinearLayout> loginForm = new WeakReference<>(registerNextStep2);
//        registerNext.updateData(RegisterNext.IS_LOADING, show);
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            registerNextStatus.setVisibility(View.VISIBLE);
            registerNextStatus.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(loginstatus.get()!=null)
                                loginstatus.get().setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                        }
                    });

            registerNextStep2.setVisibility(View.VISIBLE);
            registerNextStep2.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(loginForm.get()!=null)
                                loginForm.get().setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registerNextStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            registerNextStep2.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER_NEXT;
    }

    @Override
    public void setData(int type, Bundle data) {
        String email = (String)presenter.getData(RegisterNewNext.EMAIl);
        String password = (String)presenter.getData(RegisterNewNext.PASSWORD);
        switch (type){
            case DownloadService.REGISTER:

                TrackingUtils.fragmentBasedAFEvent(SessionRouter.IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT);

                RegisterSuccessModel registerSuccessModel = data.getParcelable(DownloadService.REGISTER_MODEL_KEY);
                switch (registerSuccessModel.getIsActive()){
                    case RegisterSuccessModel.USER_PENDING:
                        sendLocalyticsRegisterEvent(registerSuccessModel.getUserId());
                        sendGTMRegisterEvent();
                        Bundle bundle = new Bundle();
                        bundle.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, DownloadService.REGISTER_WEBVIEW);
                        ((SessionView)getActivity()).moveToActivationResend(email, bundle);
                        break;
                    case RegisterSuccessModel.USER_ACTIVE:
                        LoginViewModel loginViewModel = new LoginViewModel();
                        loginViewModel.setUsername(email);
                        loginViewModel.setPassword(password);
                        LoginImpl.login(DownloadService.LOGIN_ACCOUNTS_TOKEN, getActivity(), LoginModel.EmailType, loginViewModel);
                        break;
                    default:
                        break;
                }
                break;
            case DownloadService.MAKE_LOGIN:
                if(new SessionHandler(getActivity()).isV4Login()) {// go back to home
                    TrackingUtils.eventLoca(getString(R.string.event_register) + " with e-mail");
                        CommonUtils.dumper("LocalTag : DEFAULT REGISTER");
                    getActivity().startActivity(new Intent(getActivity(),
                            HomeRouter.getHomeActivityClass())
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        getActivity().finish();
                }
                break;
        }
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        SnackbarManager.make(getActivity(), getString(R.string.message_verification_timeout), Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String)data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String)data[0];
        SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG).show();
        showProgress(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        KeyboardHandler.DropKeyboard(getActivity(),getView());
    }

    private void sendLocalyticsRegisterEvent(int userId){
        Map<String, String> attributesLogin = new HashMap<String, String>();
        CustomerWrapper customerLogin = new CustomerWrapper();
        customerLogin.setCustomerId(Integer.toString(userId));
        customerLogin.setFullName((String)presenter.getData(RegisterNewNext.FULLNAME));
        customerLogin.setEmailAddress((String)presenter.getData(RegisterNewNext.EMAIl));
        customerLogin.setExtraAttr(attributesLogin);
        customerLogin.setMethod(getString(R.string.title_email));
        UnifyTracking.eventLoginLoca(customerLogin);
    }
    private void sendGTMRegisterEvent(){
        UnifyTracking.eventRegisterSuccess(getString(R.string.title_email));
    }

    private void sendGTMClickStepTwo(){
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_2);
    }

    private void sendGTMRegisterError(String label){
        UnifyTracking.eventRegisterError(label);
    }
}
