package com.tokopedia.session.register.view.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.activity.ActivationActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.view.adapter.AutoCompleteTextAdapter;
import com.tokopedia.session.register.view.di.RegisterEmailDependencyInjector;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenter;
import com.tokopedia.session.register.view.util.RegisterUtil;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;
import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;
import com.tokopedia.util.CustomPhoneNumberUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

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
public class RegisterEmailFragment extends BaseDaggerFragment
        implements RegisterEmailViewListener, RegisterConstant {

    private static final int REQUEST_AUTO_LOGIN = 101;
    private static final int REQUEST_ACTIVATE_ACCOUNT = 102;

    View container;
    View redirectView;
    AutoCompleteTextView email;
    TextInputEditText registerPassword;
    TextView registerButton;
    EditText phone;
    TkpdHintTextInputLayout wrapperName;
    TkpdHintTextInputLayout wrapperEmail;
    TkpdHintTextInputLayout wrapperPassword;
    TkpdHintTextInputLayout wrapperPhone;
    EditText name;
    TextView registerNextTAndC;

    TkpdProgressDialog progressDialog;
    RegisterEmailPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    public static RegisterEmailFragment createInstance() {
        return new RegisterEmailFragment();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = RegisterEmailDependencyInjector.getPresenter(this);

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_email, parent, false);
        container = view.findViewById(R.id.container);
        redirectView = view.findViewById(R.id.redirect_reset_password);
        email = view.findViewById(R.id.register_email);
        registerPassword = view.findViewById(R.id.register_password);
        registerButton = view.findViewById(R.id.register_button);
        phone = view.findViewById(R.id.register_next_phone_number);
        wrapperName = view.findViewById(R.id.wrapper_name);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        wrapperPhone = view.findViewById(R.id.wrapper_phone);
        name = view.findViewById(R.id.name);
        registerNextTAndC = view.findViewById(R.id.register_next_detail_t_and_p);

        prepareView(view);
        setViewListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    private void prepareView(View view) {
        String joinString = getString(com.tokopedia.core.R.string.detail_term_and_privacy) +
                "<br>" + getString(com.tokopedia.core.R.string.link_term_condition) +
                " serta " + getString(com.tokopedia.core.R.string.link_privacy_policy);

        registerNextTAndC.setText(MethodChecker.fromHtml(joinString));
        registerNextTAndC.setMovementMethod(LinkMovementMethod.getInstance());
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
        email.addTextChangedListener(emailWatcher(wrapperEmail));
        registerPassword.addTextChangedListener(passwordWatcher(wrapperPassword));
        name.addTextChangedListener(nameWatcher(wrapperName));
        phone.addTextChangedListener(phoneWatcher(wrapperPhone));
        phone.addTextChangedListener(phoneWatcher(phone));

        if (sessionHandler != null &&
                sessionHandler.isV4Login()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    private TextWatcher nameWatcher(final TkpdHintTextInputLayout wrapper) {
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
                } else if (RegisterUtil.checkRegexNameLocal(name.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.error_illegal_character));
                } else if (RegisterUtil.isExceedMaxCharacter(name.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.error_max_35_character));

                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher passwordWatcher(final TkpdHintTextInputLayout wrapper) {
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
                } else if (registerPassword.getText().toString().length() < PASSWORD_MINIMUM_LENGTH) {
                    setWrapperError(wrapper, getString(R.string.error_invalid_password));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher emailWatcher(final TkpdHintTextInputLayout wrapper) {
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
                } else if (!CommonUtils.EmailValidation(email.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.error_invalid_email));
                }

                checkIsValidForm();
            }
        };
    }

    private TextWatcher phoneWatcher(final EditText editText) {
        return new TextWatcher() {

            private boolean backspacingFlag = false;
            private int cursorComplement;
            private int totalSpace, newTotalSpace;
            private int selectionStart;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                totalSpace = s.length() - s.toString().replace("-", "").length();
                cursorComplement = s.length() - editText.getSelectionStart();

                String cutString = s.toString().substring(0, editText.getSelectionStart());
                int totalSpaceWithinCutString = cutString.length() - cutString.replace("-", "")
                        .length();
                selectionStart = editText.getSelectionStart() - totalSpaceWithinCutString;

                if (count > after) {
                    backspacingFlag = true;
                } else {
                    backspacingFlag = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String unformattedText = s.toString().replace("-", "");
                String formattedText = CustomPhoneNumberUtil.transform(unformattedText);
                newTotalSpace = formattedText.length() - unformattedText.length();


                if (s.toString().length() > 4 && cursorComplement == 0) {
                    formatPhoneNumber(formattedText, formattedText.length());
                } else if (s.toString().length() > 4 && !backspacingFlag && cursorComplement > 0) {
                    int cursorPosition = formattedText.length() -
                            (cursorComplement + newTotalSpace - totalSpace);
                    if (selectionStart % 4 == 0 && selectionStart != 0) cursorPosition += 1;
                    formatPhoneNumber(formattedText, cursorPosition);
                } else if (s.toString().length() > 4 && backspacingFlag && cursorComplement > 0) {
                    int cursorPosition = s.length() - cursorComplement;
                    formatPhoneNumber(formattedText, cursorPosition);
                }

            }

            private void formatPhoneNumber(String formattedText, int cursorPosition) {
                editText.removeTextChangedListener(this);
                editText.setText(formattedText);
                editText.setSelection(cursorPosition);
                editText.addTextChangedListener(this);
            }
        };
    }


    private TextWatcher phoneWatcher(final TkpdHintTextInputLayout wrapper) {
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
                } else if (!RegisterUtil.isValidPhoneNumber(
                        phone.getText().toString().replace("-", ""))) {
                    setWrapperError(wrapper, getString(R.string.error_invalid_phone_number));

                }

                checkIsValidForm();
            }
        };
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
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                listOfAddresses.add(account.name);
            }
        }
        return new ArrayList<>(listOfAddresses);
    }

    private void setViewListener() {
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

    }

    private void checkIsValidForm() {
        if (presenter.isCanRegister()) {
            setRegisterButtonEnabled();
        } else {
            setRegisterButtonDisabled();
        }
    }

    private void setRegisterButtonEnabled() {
        MethodChecker.setBackground(registerButton, MethodChecker.getDrawable(MainApplication
                .getAppContext(), R.drawable.green_button_rounded_unify));
        registerButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                R.color.white));
        registerButton.setEnabled(true);
    }

    private void setRegisterButtonDisabled() {
        MethodChecker.setBackground(registerButton, MethodChecker.getDrawable(MainApplication
                .getAppContext(), R.drawable.grey_button_rounded));
        registerButton.setTextColor(MethodChecker.getColor(MainApplication.getAppContext(),
                R.color.grey_500));
        registerButton.setEnabled(false);
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
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
    public void resetError() {
        setWrapperError(wrapperName, null);
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);
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
        Intent intent = ActivationActivity.getCallingIntent(getActivity(),
                email.getText().toString(),
                registerPassword.getText().toString()
        );
        startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT);
    }

    @Override
    public void goToAutomaticResetPassword() {
        dismissLoadingProgress();
        startActivity(ForgotPasswordActivity.getAutomaticResetPasswordIntent(getActivity(),
                email.getText().toString()));
    }

    @Override
    public void goToAutomaticLogin() {
        Intent intentLogin = LoginActivity.getAutomaticLogin(
                getActivity(),
                email.getText().toString(),
                registerPassword.getText().toString()
        );
        startActivityForResult(intentLogin, REQUEST_AUTO_LOGIN);
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

        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessRegisterEmail());
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

    public void showInfo() {
        dismissLoadingProgress();
        TextView view = (TextView) redirectView.findViewById(R.id.body);
        final String emailString = email.getText().toString();
        String text = getString(R.string.account_registered_body, emailString);
        String part = getString(R.string.account_registered_body_part);
        Spannable spannable = getSpannable(text, part);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), text.indexOf(emailString)
                , text.indexOf(emailString) + emailString.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ForgotPasswordActivity.getCallingIntent(getActivity(), emailString));
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
        switch (requestCode) {
            case RC_SIGN_IN_GOOGLE:
                if (data != null) {
                    GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
                    email.setText(googleSignInAccount.getEmail());
                    if (googleSignInAccount.getDisplayName() != null
                            && !googleSignInAccount.getDisplayName().equals(googleSignInAccount.getEmail()))
                        name.setText(googleSignInAccount.getDisplayName());
                }
                break;
            case REQUEST_AUTO_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    dismissLoadingProgress();
                }
                break;

            case REQUEST_ACTIVATE_ACCOUNT:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else {
                    dismissLoadingProgress();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}
