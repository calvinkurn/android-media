package com.tokopedia.session.register.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customView.PasswordView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.register.activity.RegisterActivity;
import com.tokopedia.session.register.adapter.AutoCompleteTextAdapter;
import com.tokopedia.session.register.interactor.RegisterNetworkInteractorImpl;
import com.tokopedia.session.register.model.RegisterStep1ViewModel;
import com.tokopedia.session.register.presenter.RegisterStep1Presenter;
import com.tokopedia.session.register.presenter.RegisterStep1PresenterImpl;
import com.tokopedia.session.register.viewlistener.RegisterStep1ViewListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/27/17.
 */

@RuntimePermissions
public class RegisterStep1Fragment extends BasePresenterFragment<RegisterStep1Presenter>
        implements RegisterStep1ViewListener {

    @BindView(R2.id.register_email)
    AutoCompleteTextView email;

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

    public static RegisterStep1Fragment createInstance(Bundle bundle) {
        RegisterStep1Fragment fragment = new RegisterStep1Fragment();
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

        presenter = new RegisterStep1PresenterImpl(this,
                new CompositeSubscription(),
                new RegisterNetworkInteractorImpl(new AccountsService(bundle)));
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(View view) {
        final Typeface typeface = registerPassword.getTypeface();


        String sourceString = "Sudah punya akun? Masuk";

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                , 0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);

        registerNextButton.setBackgroundResource(com.tokopedia.core.R.drawable.bg_rounded_corners);
    }

    @Override
    public void onResume() {
        super.onResume();
        email.addTextChangedListener(watcher(wrapperEmail));
        registerPassword.addTextChangedListener(watcher(wrapperPassword));
        name.addTextChangedListener(watcher(wrapperName));
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void setupEmailAddressToEmailTextView() {
        List<String> list = getEmailListOfAccountsUserHasLoggedInto();
        if (list.size() > 0) {
            String mEmail = list.get(0);
            email.setText(mEmail);
            email.setSelection(mEmail.length());
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
        RegisterStep1FragmentPermissionsDispatcher.setupEmailAddressToEmailTextViewWithCheck(
                RegisterStep1Fragment.this);

        registerPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == com.tokopedia.core.R.id.register_button || id == EditorInfo.IME_NULL) {
                    presenter.registerNext();
                    return true;
                }
                return false;
            }
        });

        registerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.registerNext();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

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
    public void showSnackbar(String message) {
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {

        name.setEnabled(isEnabled);
        registerPassword.setEnabled(isEnabled);
        registerNext.setEnabled(isEnabled);
    }

    @Override
    public void onSuccessValidateEmail(boolean isActive) {
        dismissLoadingProgress();
    }


    @Override
    public void onErrorValidateEmail(String errorMessage) {
        showSnackbar(errorMessage);
    }

    @Override
    public void showLoadingProgress() {
        setActionsEnabled(false);
        registerNextProgress.setVisibility(View.VISIBLE);
        registerNextButton.setText(getString(R.string.processing));
    }

    @Override
    public void dismissLoadingProgress() {
        setActionsEnabled(true);
        registerNextProgress.setVisibility(View.GONE);
        registerNextButton.setText(getString(R.string.title_next));
    }

    @Override
    public void goToRegisterStep2() {
        dismissLoadingProgress();
        RegisterStep1ViewModel pass = new RegisterStep1ViewModel();
        pass.setName(name.getText().toString());
        pass.setPassword(registerPassword.getText().toString());
        pass.setEmail(email.getText().toString());
        pass.setAutoVerify(isEmailAddressFromDevice());
        if (getActivity() instanceof RegisterActivity)
            ((RegisterActivity) getActivity()).goToStep2(pass);
    }

    @Override
    public void goToActivationPage() {
        dismissLoadingProgress();
        if (getActivity() instanceof RegisterActivity)
            ((RegisterActivity) getActivity()).goToSendActivation(email.getText().toString(), name.getText().toString());
    }

    @Override
    public void goToResetPasswordPage() {
        dismissLoadingProgress();
        startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegisterStep1FragmentPermissionsDispatcher.onRequestPermissionsResult(
                RegisterStep1Fragment.this, requestCode, grantResults);
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
        presenter.onDestroyView();
    }

}
