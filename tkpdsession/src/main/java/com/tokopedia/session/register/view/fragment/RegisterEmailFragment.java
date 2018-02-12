package com.tokopedia.session.register.view.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.activity.ActivationActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.view.adapter.AutoCompleteTextAdapter;
import com.tokopedia.session.register.view.di.RegisterEmailDependencyInjector;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenter;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;
import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;
import com.tokopedia.session.session.activity.Login;

import net.hockeyapp.android.Tracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * Created by nisie on 1/27/17.
 */

@RuntimePermissions
public class RegisterEmailFragment extends BasePresenterFragment<RegisterEmailPresenter>
        implements RegisterEmailViewListener, RegisterConstant {

    @BindView(R2.id.container)
    View container;

    @BindView(R2.id.redirect_reset_password)
    View redirectView;

    @BindView(R2.id.register_email)
    AutoCompleteTextView email;

    @BindView(R2.id.register_password)
    TextInputEditText registerPassword;

    @BindView(R2.id.register_button)
    TextView registerButton;

    @BindView(R2.id.register_next_phone_number)
    EditText phone;

    @BindView(R2.id.wrapper_name)
    TextInputLayout wrapperName;

    @BindView(R2.id.wrapper_email)
    TextInputLayout wrapperEmail;

    @BindView(R2.id.wrapper_password)
    TextInputLayout wrapperPassword;

    @BindView(R2.id.wrapper_phone)
    TextInputLayout wrapperPhone;

    @BindView(R2.id.login_button)
    TextView loginButton;

    @BindView(R2.id.name)
    EditText name;

    @BindView(R2.id.register_next_detail_t_and_p)
    TextView registerNextTAndC;

    TkpdProgressDialog progressDialog;

    public static RegisterEmailFragment createInstance() {
        return new RegisterEmailFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
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
        presenter = RegisterEmailDependencyInjector.getPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register_email;
    }

    @Override
    protected void initView(View view) {
        final Typeface typeface = registerPassword.getTypeface();

        String sourceString = "Sudah punya akun? Masuk";

        Spannable spannable = getSpannable(sourceString, "Masuk");

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
        showTermsAndOptionsTextView();

    }

    private Spannable getSpannable(String sourceString, String hyperlinkString) {
        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf(hyperlinkString)
                , sourceString.length()
                , 0);


        return spannable;
    }

    private void showTermsAndOptionsTextView() {
        String joinString = context.getString(com.tokopedia.core.R.string.detail_term_and_privacy) +
                " " + context.getString(com.tokopedia.core.R.string.link_term_condition) +
                ", serta " + context.getString(com.tokopedia.core.R.string.link_privacy_policy);

        registerNextTAndC.setText(MethodChecker.fromHtml(joinString));
        registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(NAME, ""));
            phone.setText(savedInstanceState.getString(PHONE, ""));
            email.setText(savedInstanceState.getString(EMAIL, ""));
            registerPassword.setText(savedInstanceState.getString(PASSWORD, ""));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        email.addTextChangedListener(watcher(wrapperEmail));
        registerPassword.addTextChangedListener(watcher(wrapperPassword));
        name.addTextChangedListener(watcher(wrapperName));
        phone.addTextChangedListener(watcher(wrapperPhone));
        phone.addTextChangedListener(watcher(phone));
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void setupEmailAddressToEmailTextView() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        if (list.size() > 0) {
            String mEmail = list.get(0);
            if (email.getText().toString().equals("")) {
                email.setText(mEmail);
                email.setSelection(mEmail.length());
            }
        }
        email.setThreshold(0);
        AutoCompleteTextAdapter adapter = new AutoCompleteTextAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                list,
                new AutoCompleteTextAdapter.onTextSelectedListener() {
                    @Override
                    public void onSelected(String selected) {
                        email.setText(selected);
                        email.setSelection(selected.length());
                        email.dismissDropDown();
                    }
                });
        email.setAdapter(adapter);
    }

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
    protected void setViewListener() {
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!email.isPopupShowing()) {
                    email.showDropDown();
                }
                return false;
            }
        });
        RegisterEmailFragmentPermissionsDispatcher
                .setupEmailAddressToEmailTextViewWithCheck(RegisterEmailFragment.this);

        registerPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == com.tokopedia.core.R.id.register_button || id == EditorInfo.IME_NULL) {
                    presenter.onRegisterClicked();
                    return true;
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRegisterClicked();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        getActivity().finish();

        startActivity(Login.getCallingIntent(
                getActivity())
        );
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

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
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                }
            }
        };
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
    public EditText getName() {
        return name;
    }

    @Override
    public EditText getEmail() {
        return email;
    }

    @Override
    public EditText getPassword() {
        return registerPassword;
    }

    @Override
    public EditText getPhone() {
        return phone;
    }

    @Override
    public void setPhoneError(String errorMessage) {
        setWrapperError(wrapperPhone, errorMessage);
        phone.requestFocus();
    }

    @Override
    public void resetError() {
        setWrapperError(wrapperName, null);
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);
    }

    @Override
    public void setPasswordError(String messageError) {
        setWrapperError(wrapperPassword, messageError);
        registerPassword.requestFocus();
    }

    @Override
    public void setEmailError(String messageError) {
        setWrapperError(wrapperEmail, messageError);
        email.requestFocus();
    }

    @Override
    public void setNameError(String messageError) {
        setWrapperError(wrapperName, messageError);
        name.requestFocus();
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {

        email.setEnabled(isEnabled);
        phone.setEnabled(isEnabled);
        name.setEnabled(isEnabled);
        registerPassword.setEnabled(isEnabled);
        registerButton.setEnabled(isEnabled);
    }

    @Override
    public void showLoadingProgress() {
        setActionsEnabled(false);
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null && getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void dismissLoadingProgress() {
        setActionsEnabled(true);

        if (progressDialog != null && getActivity() != null)
            progressDialog.dismiss();
    }

    @Override
    public void goToActivationPage(RegisterEmailViewModel viewModel) {

        sendGTMRegisterEvent();

        dismissLoadingProgress();
        startActivity(ActivationActivity.getCallingIntent(getActivity(),
                email.getText().toString()
        ));
        getActivity().finish();
    }

    @Override
    public void goToAutomaticResetPassword() {
        dismissLoadingProgress();
        startActivity(ForgotPasswordActivity.getAutomaticResetPasswordIntent(getActivity(),
                email.getText().toString()));
    }

    @Override
    public void goToAutomaticLogin() {
        dismissLoadingProgress();
        getActivity().finish();

        startActivity(Login.getAutomaticLoginIntent(
                getActivity(),
                email.getText().toString(),
                registerPassword.getText().toString())
        );
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onErrorRegister(String errorMessage) {
        dismissLoadingProgress();
        setActionsEnabled(true);
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRegister(RegisterEmailViewModel registerResult) {
        dismissLoadingProgress();
        setActionsEnabled(true);
        presenter.startAction(registerResult);

    }

    @Override
    public void getRegisterModel(RegisterViewModel registerViewModel) {
        registerViewModel.setName(name.getText().toString());
        registerViewModel.setEmail(email.getText().toString());
        registerViewModel.setPhone(phone.getText().toString().replace("-", ""));
        registerViewModel.setAgreedTermCondition(true);
        registerViewModel.setPassword(registerPassword.getText().toString());
        registerViewModel.setIsAutoVerify(isEmailAddressFromDevice() ? 1 : 0);
    }

    private void sendGTMRegisterEvent() {
        UnifyTracking.eventRegisterSuccess(getString(com.tokopedia.core.R.string.title_email));
        BranchSdkUtils.sendRegisterEvent(email.getText().toString(),phone.getText().toString());
    }

    private boolean isEmailAddressFromDevice() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        boolean result = false;
        if (list.size() > 0) {
            A:
            for (String e : list) {
                if (e.equals(email.getText().toString())) {
                    result = true;
                    break A;
                }
            }
        }
        return result;
    }

    public void showInfo(){
        dismissLoadingProgress();
        TextView view = (TextView) redirectView.findViewById(R.id.body);
        final String emailString = email.getText().toString();
        String text = getString(R.string.account_registered_body, emailString);
        String part = getString(R.string.account_registered_body_part);
        Spannable spannable = getSpannable(text, part);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), text.indexOf(emailString)
                                                        , text.indexOf(emailString)+emailString.length()
                                                        , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ForgotPasswordActivity.getCallingIntent(context, emailString));
            }
        });
        redirectView.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegisterEmailFragmentPermissionsDispatcher.onRequestPermissionsResult(
                RegisterEmailFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.GET_ACCOUNTS)
    void showRationaleForGetAccounts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.GET_ACCOUNTS);
    }

    @OnPermissionDenied(Manifest.permission.GET_ACCOUNTS)
    void showDeniefForGetAccounts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }

    @OnNeverAskAgain(Manifest.permission.GET_ACCOUNTS)
    void showNeverAskForGetAccounts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unsubscribeObservable();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME, name.getText().toString());
        outState.putString(PHONE, phone.getText().toString());
        outState.putString(EMAIL, email.getText().toString());
        outState.putString(PASSWORD, registerPassword.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN_GOOGLE:
                if (data != null) {
                    GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
                    email.setText(googleSignInAccount.getEmail());
                    if(googleSignInAccount.getDisplayName() != null
                            && !googleSignInAccount.getDisplayName().equals(googleSignInAccount.getEmail()))
                        name.setText(googleSignInAccount.getDisplayName());
                }
                break;
        }
    }
}
