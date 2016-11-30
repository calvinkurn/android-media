package com.tokopedia.session.session.fragment;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.customView.PasswordView;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.session.session.presenter.RegisterNew;
import com.tokopedia.session.session.presenter.RegisterNewImpl;
import com.tokopedia.session.session.presenter.RegisterNewView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.NetworkUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by m.normansyah on 1/22/16.
 * 25-1-2016, start not rotatable register
 * features :
 * 1. list of email account from devices.
 * 2. show password
 */
//@RuntimePermissions
public class RegisterNewViewFragment extends BaseFragment<RegisterNew> implements RegisterNewView {
    public static final int PASSWORD_MINIMUM_LENGTH = 6;
    public static final String VALIDATE_EMAIL = "validate_email";

    @BindView(R2.id.register_name)
    AutoCompleteTextView registerName;
    @BindView(R2.id.register_password)
    PasswordView registerPassword;
    @BindView(R2.id.register_next_progress)
    ProgressBar registerNextProgress;
    @BindView(R2.id.register_next)
    RelativeLayout registerNext;
    @BindView(R2.id.register_next_button)
    TextView registerNextButton;
    @BindView(R2.id.register_status)
    LinearLayout registerStatus;
    @BindView(R2.id.register_status_message)
    TextView registerStatusMessage;
    @BindView(R2.id.wrapper_name)
    TextInputLayout wrapperName;
    @BindView(R2.id.wrapper_email)
    TextInputLayout wrapperEmail;
    @BindView(R2.id.wrapper_password)
    TextInputLayout wrapperPassword;

    @BindView(R2.id.login_button)
    TextView loginButton;


    @BindView(R2.id.name)
    EditText name;

    @BindView(R2.id.linearLayout) LinearLayout linearLayout;
    Context mContext;

    Snackbar snackbar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void showErrorValidateEmail() {
//        showProgress(false);
        showErrorValidateEmail(getString(R.string.alert_email_address_is_already_registered));
        presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.EMAIL);
    }

    @Override
    public void showErrorValidateEmail(String err) {
        snackbar = SnackbarManager.make(RegisterNewViewFragment.this.getActivity()
                ,err, Snackbar.LENGTH_LONG);
        snackbar.show();
        enableDisableAllFieldsForEmailValidationForm(true);

        presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.EMAIL);
    }

    @Override
    public void moveToRegisterNext(String name, String email, String password) {
//        IsSaving = false;
        if(snackbar!=null){
            snackbar.dismiss();
        }
        if(getActivity()!=null&&getActivity() instanceof SessionView){
            ((SessionView)getActivity()).moveToNewRegisterNext(name, email, password, isEmailAddressFromDevice());
        }
    }

    @Override
    public void alertBox() {
        alertbox(getString(R.string.alert_on_email_edit_for_first_time));
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        if(data.containsKey(RegisterNew.EMAIL)){
            String email = (String)data.get(RegisterNew.EMAIL);
            registerName.setText(email);
        }else if(data.containsKey(RegisterNew.PASSWORD)){
            String password = (String)data.get(RegisterNew.PASSWORD);
            registerPassword.setText(password);
        }
    }

    public static Fragment newInstance(){
        return new RegisterNewViewFragment();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @Override
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        Log.d("steven check", String.valueOf(show));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            registerStatus.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(registerStatus!=null)
                                registerStatus.setVisibility(show ? View.VISIBLE
                                        : View.GONE);
                        }
                    });


            linearLayout.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (linearLayout != null)
                                linearLayout.setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                        }
                    });

            loginButton.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (loginButton != null)
                                loginButton.setVisibility(show ? View.GONE
                                        : View.VISIBLE);
                        }
                    });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registerStatus.setVisibility(show ? View.VISIBLE : View.GONE);
            linearLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View parentView = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        showProgress(false);
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerName.addTextChangedListener(watcher(wrapperEmail));
        registerPassword.addTextChangedListener(watcher(wrapperPassword));
        name.addTextChangedListener(watcher(wrapperName));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    public void attemptRegisterStep1() {
//        if (IsSaving) {
//            return;
//        }
        // Reset errors.
        setWrapperError(wrapperName,null);
        setWrapperError(wrapperEmail,null);
        setWrapperError(wrapperPassword,null);


        // Store values at the time of the login attempt.
        String mName = name.getText().toString();
        String mEmail = registerName.getText().toString();
        String mPassword = registerPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password
        if (TextUtils.isEmpty(mPassword)) {
            setWrapperError(wrapperPassword, getString(R.string.error_field_required));
            focusView = registerPassword;
            cancel = true;
            presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.PASSWORD);
        } else if (mPassword.length() < PASSWORD_MINIMUM_LENGTH) {
            setWrapperError(wrapperPassword, getString(R.string.error_invalid_password));
            focusView = registerPassword;
            cancel = true;
            presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.PASSWORD);
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            setWrapperError(wrapperEmail, getString(R.string.error_field_required));
            focusView = registerName;
            cancel = true;
            presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.EMAIL);
        } else if (!CommonUtils.EmailValidation(mEmail)) {
            setWrapperError(wrapperEmail, getString(R.string.error_invalid_email));
            focusView = registerName;
            cancel = true;
            presenter.sendGTMRegisterError(getActivity(), AppEventTracking.EventLabel.EMAIL);
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(mName)) {
            setWrapperError(wrapperName, getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
//            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
        }else if(RegisterNewImpl.RegisterUtil.checkRegexNameLocal(mName)){
            setWrapperError(wrapperName, getString(R.string.error_illegal_character));
            focusView = name;
            cancel = true;
//            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
        }else if(RegisterNewImpl.RegisterUtil.isExceedMaxCharacter(mName)){
            setWrapperError(wrapperName, getString(R.string.error_max_35_character));
            focusView = name;
            cancel = true;
//            sendGTMRegisterError(AppEventTracking.EventLabel.FULLNAME);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            validateEmailAddressTask();
        }
    }

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
    public void validateEmailAddressTask() {
        if (!NetworkUtil.isConnected(getActivity())) {
            alertbox(getString(R.string.alert_check_your_internet_connection));
            return;
        }
        enableDisableAllFieldsForEmailValidationForm(false);

        presenter.validateEmail(getActivity(), name.getText().toString(),
                registerName.getText().toString(), registerPassword.getText().toString());
    }

    @OnClick(R2.id.register_next)
    public void registerNext(){
        attemptRegisterStep1();
    }

    @OnClick(R2.id.login_button)
    public void moveToLogin(){
        ((SessionView) mContext).moveToLogin();
    }

    @Override
    public void enableDisableAllFieldsForEmailValidationForm(boolean isEnable) {
        if (isEnable) {
            registerNextProgress.setVisibility(View.GONE);
            registerNextButton.setText(getString(R.string.title_next));
            registerName.setEnabled(true);
            registerPassword.setEnabled(true);
            registerNext.setEnabled(true);
        } else {
            registerNextProgress.setVisibility(View.VISIBLE);
            registerNextButton.setText(getString(R.string.processing));
            registerName.setEnabled(false);
            registerPassword.setEnabled(false);
            registerNext.setEnabled(false);
        }
    }

    @Override
    public void initView() {
        final Typeface typeface = registerPassword.getTypeface();
        registerName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!registerName.isPopupShowing()) {
                    registerName.showDropDown();
                }
                return false;
            }
        });
        setupEmailAddressToEmailTextView();
//      registerName.addTextChangedListener((TextWatcher) presenter);

        registerPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.register_button || id == EditorInfo.IME_NULL) {
                    registerNext();
                    return true;
                }
                return false;
            }
        });

        String sourceString = "Sudah punya akun? Masuk";

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                ,0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);

        registerNextButton.setBackgroundResource(R.drawable.bg_rounded_corners);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribeFacade();
        KeyboardHandler.DropKeyboard(getActivity(),getView());
        unbinder.unbind();
        dismissSnackbar();
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterNewImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_reborn;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDatas();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveDatas();
        super.onSaveInstanceState(outState);
    }

    private void saveDatas() {
        if(registerName!=null){
            presenter.saveData(RegisterNewImpl.convertToMap(RegisterNew.EMAIL, registerName.getText().toString()));
        }
        if(registerPassword!=null) {
            presenter.saveData(RegisterNewImpl.convertToMap(RegisterNew.PASSWORD, registerPassword.getText().toString()));
        }
    }

    @Override
    public List<String> getEmailListOfAccountsUserHasLoggedInto() {
        Set<String> listOfAddresses = new LinkedHashSet<>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccountsByType("com.google");
        if (accounts != null) {
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    listOfAddresses.add(account.name);
                }
            }
        }
        return new ArrayList<>(listOfAddresses);
    }

    @Override
    public boolean isEmailAddressFromDevice() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        boolean result = false;
        if (list.size() > 0) {
            A : for(String email:list) {
                if (email.equals(registerName.getText().toString())) {
                    result = true;
                    break A;
                }
            }
        }
        return result;
    }

    @Override
    public void alertbox(String mymessage)
    {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity())
                .setMessage(mymessage)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void setupEmailAddressToEmailTextView() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        if (list.size() > 0) {
            String mEmail = list.get(0);
            registerName.setText(mEmail);
            registerName.setSelection(mEmail.length());
        }
        registerName.setThreshold(0);
        AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        registerName.setAdapter(adapter);
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void setData(int type, Bundle data) {
        if (presenter != null)
            presenter.setData(getActivity(),type, data);
//        throw new UnsupportedOperationException("need to implement this !!");
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        showDialog(text);
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        showProgress(false);
        //[START] move to activation resent
        if (text.contains("belum diaktivasi")) {
            if (mContext != null && mContext instanceof SessionView) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, DownloadService.REGISTER_WEBVIEW);
                ((SessionView) mContext).moveToActivationResend(registerName.getText().toString(),bundle);
            }
        }
        switch (type){
            default:
                ((SessionView)mContext).showError(text);
                break;
        }
        //[END] move to activation resent
        registerPassword.setText("");
    }

    public void showDialog(String dialogText) {
        snackbar = SnackbarManager.make(getActivity(), dialogText, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
    * This class shows auto complete textview hints and handles selection plus dismiss events of drop down.
    */
    class AutoCompleteTextViewAdapter extends ArrayAdapter<String> {

        public AutoCompleteTextViewAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            final TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    isSelectedFromAutoCompleteTextView = true;
                    registerName.setText(textView.getText().toString());
                    registerName.setSelection(textView.getText().toString().length());
                    registerName.dismissDropDown();
                }
            });
            return view;
        }
    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(),string, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void finishActivity() {
        if(new SessionHandler(getActivity()).isV4Login()) {// go back to home
            getActivity().startActivity(new Intent(getActivity(), HomeRouter.getHomeActivityClass()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void dismissSnackbar() {
        if(snackbar!=null) snackbar.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(this);
    }
}