package com.tokopedia.session.session.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.session.activation.view.activity.ActivationActivity;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.session.session.presenter.Login;
import com.tokopedia.session.session.presenter.LoginImpl;
import com.tokopedia.session.session.presenter.LoginView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT;
import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * @author m.normansyah
 * @since 4-11-2015
 */

/**
 * edited by steven
 */
@RuntimePermissions
public class
LoginFragment extends Fragment implements LoginView {

    private static final String REGISTER = "Daftar";
    private static final int REQUEST_PHONE_NUMBER = 101;

    // demo only
    int anTestInt = 0;
    Login login;

    Context mContext;

    View rootView;
    @BindView(R2.id.email_auto)
    AutoCompleteTextView mEmailView;
    @BindView(R2.id.password)
    TextInputEditText mPasswordView;
    @BindView(R2.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R2.id.login_status)
    RelativeLayout mLoginStatusView;
    @BindView(R2.id.login_status_message)
    TextView mLoginStatusMessageView;
    @BindView(R2.id.register_button)
    TextView registerButton;
    @BindView(R2.id.forgot_pass)
    TextView forgotPass;
    @BindView(R2.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R2.id.accounts_sign_in)
    TextView accountSignIn;
    @BindView(R2.id.wrapper_email)
    TextInputLayout wrapperEmail;
    @BindView(R2.id.wrapper_password)
    TextInputLayout wrapperPassword;
    @BindView(R2.id.remember_account)
    CheckBox rememberAccount;

    ArrayAdapter<String> autoCompleteAdapter;
    List<LoginProviderModel.ProvidersBean> listProvider;
    Snackbar snackbar;
    private Unbinder unbinder;
    private CallbackManager callbackManager;

    public static LoginFragment newInstance(String mEmail, boolean goToIndex, String login, String name, String url) {
        Bundle extras = new Bundle();
        extras.putString("mEmail", mEmail);
        extras.putBoolean("goToIndex", goToIndex);
        extras.putString("login", login);
        extras.putString("name", name);
        extras.putString("url", url);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(extras);
        return loginFragment;
    }

    public LoginFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = new LoginImpl(this);
        login.initLoginInstance(mContext);
        login.fetchDataAfterRotate(savedInstanceState);
        login.fetchIntenValues(getArguments());

        UserAuthenticationAnalytics.setActiveLogin();

        if (savedInstanceState != null)
            Log.d(TAG, LoginFragment.class.getSimpleName() + " : get testing data : " + (anTestInt = savedInstanceState.getInt(TEST_INT_KEY)));

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_reborn, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        forgotPass.setPaintFlags(forgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setListener();
//        setRememberAccountState();

        String temp = getArguments().getString("login");
        if (!TextUtils.isEmpty(temp)) {
            if (temp.equals(DownloadService.FACEBOOK)) {
                onFacebookClick();
            } else if (temp.equals(DownloadService.GOOGLE)) {
                LoginFragmentPermissionsDispatcher.onGooglePlusClickedWithCheck(LoginFragment.this);
            } else if (temp.equals(DownloadService.WEBVIEW)) {
                String url = getArguments().getString("url");
                String name = getArguments().getString("name");
                loginProvideOnClick(url, name);
            }
        } else {

            setSmartLock(SmartLockActivity.RC_READ);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        login.initData();
        ScreenTracking.screen(AppScreen.SCREEN_LOGIN);
        mEmailView.addTextChangedListener(watcher(wrapperEmail));
        mPasswordView.addTextChangedListener(watcher(wrapperPassword));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        login.unSubscribe();
        unbinder.unbind();
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        dismissSnackbar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // this is for testing only
        outState.putInt(TEST_INT_KEY, ++anTestInt);
        if (mEmailView != null && mPasswordView != null && listProvider != null)
            login.saveDatabeforeRotate(outState, mEmailView.getText().toString(), mPasswordView.getText().toString());
        else
            login.saveDatabeforeRotate(outState, "", "");
    }

    @Override
    public void setAutoCompleteAdapter(List<String> LoginIdList) {
        autoCompleteAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, LoginIdList);
        mEmailView.setAdapter(autoCompleteAdapter);
    }


    @Override
    public void setEmailText(String text) {
        mEmailView.setText(text);
    }

    @Override
    public void startLoginWithGoogle(String LoginType, Object model) {
        login.startLoginWithGoogle(LoginType, model);
    }

    @Override
    public void setListener() {

        String sourceString = getString(R.string.register_text_login);

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf(REGISTER)
                , sourceString.length()
                , 0);

        registerButton.setText(spannable, TextView.BufferType.SPANNABLE);


        mPasswordView.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.login || id == EditorInfo.IME_NULL) {
                            FocusPair focusPair = validateSignIn();
                            if (focusPair.isFocus()) {
                                focusPair.getView().requestFocus();
                            } else {
                                // Show a progress spinner, and kick off a background task to
                                // perform the user login attempt.
                                mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                                KeyboardHandler.DropKeyboard(mContext, mEmailView);
                                LoginViewModel model = new LoginViewModel();
                                if (mPasswordView != null && mEmailView != null) {
//                                    model.setUsername(mEmailView.getText().toString());
//                                    model.setPassword(mPasswordView.getText().toString());
//                                    model.setIsEmailClick(true);
//                                    login.sendDataFromInternet(LoginModel.EmailType, model);
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                });
        mEmailView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mPasswordView.requestFocus();
            }
        });


        accountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save last input email
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
                if (login.addAutoCompleteData(mEmailView.getText().toString())) {
                    setAutoCompleteAdapter(login.getLastLoginIdList());
                    notifyAutoCompleteAdapter();
                }

                FocusPair focusPair = validateSignIn();
                if (focusPair.isFocus()) {
                    focusPair.getView().requestFocus();
                } else {
                    // Show a progress spinner, and kick off a background task to
                    // perform the user login attempt.
                    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
                    KeyboardHandler.DropKeyboard(mContext, mEmailView);
                    LoginViewModel model = new LoginViewModel();
                    if (mPasswordView != null && mEmailView != null) {
                        model.setUsername(mEmailView.getText().toString().replaceAll(" ", ""));
                        model.setPassword(mPasswordView.getText().toString());
                        model.setIsEmailClick(true);
                        login.sendDataFromInternet(LoginModel.EmailType, model);
                        UnifyTracking.eventCTAAction();
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventRegisterThroughLogin();
                ((SessionView) getActivity()).moveToRegisterInitial();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(ForgotPasswordActivity.getCallingIntent(getActivity(), mEmailView.getText().toString()));
            }
        });

        rememberAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if (!state) {
                    login.clearSavedAccount();
                }
            }
        });
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

    public void onFacebookClick() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.FACEBOOK);
        processFacebookLogin();
    }

    private void processFacebookLogin() {
        login.doFacebookLogin(this, callbackManager);
    }

    @Override
    public void notifyAutoCompleteAdapter() {
        autoCompleteAdapter.notifyDataSetChanged();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void showProgress(final boolean isShow) {
        //[START] save progress for rotation
        login.updateViewModel(LoginViewModel.ISPROGRESSSHOW, isShow);
        if (isShow && snackbar != null) snackbar.dismiss();
        //[END] save progress for rotation

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(isShow ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //[END] save progress for rotation
                            if (mLoginStatusView != null)
                                mLoginStatusView.setVisibility(isShow ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(isShow ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //[END] save progress for rotation
                            if (mLoginFormView != null)
                                mLoginFormView.setVisibility(isShow ? View.GONE
                                        : View.VISIBLE);
                        }
                    });

            registerButton.setVisibility(View.VISIBLE);
            registerButton.animate().setDuration(shortAnimTime)
                    .alpha(isShow ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //[END] save progress for rotation
                            if (registerButton != null)
                                registerButton.setVisibility(isShow ? View.GONE
                                        : View.VISIBLE);
                        }
                    });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(isShow ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(isShow ? View.GONE : View.VISIBLE);
            registerButton.setVisibility(isShow ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public FocusPair validateSignIn() {
        // Reset errors.
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);

        // Store values at the time of the login attempt.
        Log.d(TAG, messageTAG + " login : " + login);

        String email = mEmailView.getText().toString().replaceAll(" ", "");
        String password = mPasswordView.getText().toString();

        FocusPair focusPair = new FocusPair();

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            setWrapperError(wrapperPassword, getString(R.string.error_field_required));
            focusPair.setView(mPasswordView);
            focusPair.setIsFocus(true);
            UnifyTracking.eventLoginError(AppEventTracking.EventLabel.PASSWORD);
        } else if (password.length() < 4) {
            setWrapperError(wrapperPassword, getString(R.string.error_incorrect_password));
            focusPair.setView(mPasswordView);
            focusPair.setIsFocus(true);
            UnifyTracking.eventLoginError(AppEventTracking.EventLabel.PASSWORD);
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            setWrapperError(wrapperEmail, getString(R.string.error_field_required));
            focusPair.setView(mEmailView);
            focusPair.setIsFocus(true);
            UnifyTracking.eventLoginError(AppEventTracking.EventLabel.EMAIL);
        } else if (!CommonUtils.EmailValidation(email)) {
            setWrapperError(wrapperEmail, getString(R.string.error_invalid_email));
            focusPair.setView(mEmailView);
            focusPair.setIsFocus(true);
            UnifyTracking.eventLoginError(AppEventTracking.EventLabel.EMAIL);
        }

        return focusPair;
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
    public void moveToFragmentSecurityQuestion(int security1, int security2, int userId, String email) {
        if (mContext != null) {// && !((AppCompatActivity)mContext).isFinishing()
            ((SessionView) mContext).moveToFragmentSecurityQuestion(security1, security2, userId, email);
        }
    }

    public class FocusPair {
        View view;
        boolean isFocus;

        public FocusPair() {
        }

        public FocusPair(View view, boolean isFocus) {
            this.view = view;
            this.isFocus = isFocus;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public boolean isFocus() {
            return isFocus;
        }

        public void setIsFocus(boolean isFocus) {
            this.isFocus = isFocus;
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.LOGIN;
    }

    @Override
    public void showDialog(String dialogText) {
        snackbar = SnackbarManager.make(getActivity(), dialogText, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void destroyActivity() {
        Log.d(getClass().getSimpleName(), "destroyActivity");
        if (getActivity() != null && getActivity() instanceof SessionView) {
            ((SessionView) getActivity()).destroy();
        }
    }

    private LoginProviderModel.ProvidersBean getLoginPhoneNumberBean() {
        LoginProviderModel.ProvidersBean phoneNumberBean = new LoginProviderModel.ProvidersBean();
        phoneNumberBean.setColor("#FFFFFF");
        phoneNumberBean.setName(getString(com.tokopedia.session.R.string.phone_number));
        phoneNumberBean.setId("tokocash");
        phoneNumberBean.setImage("");
        phoneNumberBean.setImageResource(com.tokopedia.session.R.drawable.ic_phone);
        return phoneNumberBean;
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        accountSignIn.setEnabled(true);
        registerButton.setEnabled(true);
        listProvider = data;
        if (listProvider != null && checkHasNoProvider()) {
            login.saveProvider(listProvider);
            if (!GlobalConfig.isSellerApp())
                listProvider.add(2, getLoginPhoneNumberBean());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 20, 0, 15);

            for (int i = 0; i < listProvider.size(); i++) {
                String color = listProvider.get(i).getColor();
                int colorInt;
                if (color == null) {
                    colorInt = Color.parseColor("#FFFFFF");
                } else {
                    colorInt = Color.parseColor(color);
                }
                LoginTextView tv = new LoginTextView(getActivity(), colorInt);
                tv.setTextLogin(listProvider.get(i).getName());
                if (!TextUtils.isEmpty(listProvider.get(i).getImage())) {
                    tv.setImage(listProvider.get(i).getImage());
                } else if (listProvider.get(i).getImageResource() != 0) {
                    tv.setImageResource(listProvider.get(i).getImageResource());
                }
                tv.setRoundCorner(10);
                if (listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
                            onFacebookClick();
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("gplus")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
                            LoginFragmentPermissionsDispatcher.onGooglePlusClickedWithCheck(LoginFragment.this);
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("tokocash")) {
                    final int finalI = i;

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnifyTracking.eventCTAAction(listProvider.get(finalI).getName());
                            goToLoginWithPhoneNumber();
                        }
                    });
                } else {
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginProvideOnClick(listProvider.get(finalI).getUrl(),
                                    listProvider.get(finalI).getName());
                            UnifyTracking.eventCTAAction(listProvider.get(finalI).getName());
                        }
                    });
                }
                if (linearLayout != null) {
                    linearLayout.addView(tv, linearLayout.getChildCount(), layoutParams);
                }
            }
        }
    }

    private void goToLoginWithPhoneNumber() {
        Intent intent = LoginPhoneNumberActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_PHONE_NUMBER);
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void onGooglePlusClicked() {
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.GMAIL);
        showProgress(true);
        Intent intent = GoogleSignInActivity.getSignInIntent(getActivity());
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void addProgressbar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout != null && !(linearLayout.getChildAt(lastPos) instanceof ProgressBar))
            linearLayout.addView(pb, linearLayout.getChildCount());
    }

    @Override
    public void removeProgressBar() {
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout != null && linearLayout.getChildAt(lastPos) instanceof ProgressBar)
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
    }

    public boolean checkHasNoProvider() {
        if (linearLayout != null) {
            for (int i = linearLayout.getChildCount() - 1; i >= 0; i--) {
                if (linearLayout.getChildAt(i) instanceof LoginTextView) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(), string, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void triggerSaveAccount() {
        login.setRememberAccountState(rememberAccount.isChecked());

        if (login.getSavedAccountState()) {
            login.saveAccountInfo(mEmailView.getText().toString(),
                    mPasswordView.getText().toString());
        } else {
            login.clearSavedAccount();
        }
    }

    @Override
    public void triggerClearCategoryData() {
        ((TkpdCoreRouter) getActivity().getApplication()).invalidateCategoryMenuData();
    }

    private void loginProvideOnClick(final String url, final String name) {
        WebViewLoginFragment newFragment = WebViewLoginFragment
                .createInstance(url);
        newFragment.setTargetFragment(LoginFragment.this, 100);
        newFragment.show(getFragmentManager().beginTransaction(), "dialog");
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(name);
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        Log.d(TAG, messageTAG + " arise !!!");
        showDialog(getString(R.string.message_verification_timeout));
        showProgress(false);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        showProgress(false);

        if (checkEmailHasBeenActive(text)) {
            if (mContext != null && mContext instanceof SessionView) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, DownloadService.LOGIN_WEBVIEW);
                startActivity(ActivationActivity.getCallingIntent(getActivity(), mEmailView.getText().toString()));
                getActivity().finish();
            }
        }
        switch (type) {
            case DownloadService.DISCOVER_LOGIN:
                removeProgressBar();
                snackbar = SnackbarManager.make(getActivity(), "Gagal mendownload provider", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Coba lagi", retryDiscover());
                snackbar.show();
                accountSignIn.setEnabled(false);
                registerButton.setEnabled(false);
                break;
            default:
                snackbar = SnackbarManager.make(getActivity(), text, Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
        mPasswordView.setText("");
        setWrapperError(wrapperPassword, null);
    }

    private boolean checkEmailHasBeenActive(String text) {
        return text.contains("belum diaktivasi");
    }

    private View.OnClickListener retryDiscover() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.getProvider();
            }
        };
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        String text = (String) data[0];
        showDialog(text);
        showProgress(false);
    }

    @Override
    public void setData(int type, Bundle data) {
        if (login != null)
            login.setData(type, data);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 100:
                    if (resultCode == Activity.RESULT_CANCELED) {
                        KeyboardHandler.DropKeyboard(getActivity(), getView());
                        break;
                    }
                    Bundle bundle = data.getBundleExtra("bundle");
                    if (bundle.getString("path").contains("error")) {
                        snackbar = SnackbarManager.make(getActivity(), bundle.getString("message"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else if (bundle.getString("path").contains("code")) {
                        login.sendDataFromInternet(LoginModel.WebViewType, bundle);
                    } else if (bundle.getString("path").contains("activation-social")) {
                        Bundle lbundle = new Bundle();
                        lbundle.putInt(AppEventTracking.GTMKey.ACCOUNTS_TYPE, DownloadService.REGISTER_WEBVIEW);
                        startActivity(ActivationActivity.getCallingIntent(getActivity(), mEmailView.getText().toString()));
                        getActivity().finish();

                    }
                    break;
                case 200:
                    if (resultCode == Activity.RESULT_OK) {
                        mEmailView.setText(data.getExtras().getString(SmartLockActivity.USERNAME));
                        mPasswordView.setText(data.getExtras().getString(SmartLockActivity.PASSWORD));
                        accountSignIn.performClick();
                    } else if (resultCode == SmartLockActivity.RC_SAVE) {
                        destroyActivity();
                    } else if (resultCode == SmartLockActivity.RC_SAVE_SECURITY_QUESTION) {

                    }
                    break;

                case RC_SIGN_IN_GOOGLE:
                    if (data != null) {
                        GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
                        String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);

                        LoginGoogleModel model = new LoginGoogleModel();
                        model.setFullName(googleSignInAccount.getDisplayName());
                        model.setGoogleId(googleSignInAccount.getId());
                        model.setEmail(googleSignInAccount.getEmail());
                        model.setAccessToken(accessToken);

                        startLoginWithGoogle(LoginModel.GoogleType, model);
                    } else {
                        showProgress(false);
                    }
                    break;
                case REQUEST_PHONE_NUMBER: {
                    if (resultCode == Activity.RESULT_OK) {
                        destroyActivity();
                    }
                    break;
                }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void dismissSnackbar() {
        if (snackbar != null) snackbar.dismiss();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginFragmentPermissionsDispatcher.onRequestPermissionsResult(LoginFragment.this, requestCode, grantResults);
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

    private void setRememberAccountState() {
        if (login.getSavedAccountState()) {
            rememberAccount.setChecked(true);
            mEmailView.setText(login.getSavedAccountEmail());
            mPasswordView.setText(login.getSavedAccountPassword());
        }
    }


    @Override
    public void setSmartLock(int state) {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, state);
        if (state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, mEmailView.getText().toString().replaceAll(" ", ""));
            bundle.putString(SmartLockActivity.PASSWORD, mPasswordView.getText().toString());
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, 200);
    }

    @Override
    public void setSmartLock(int state, String username, String password) {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, state);
        if (state == SmartLockActivity.RC_SAVE_SECURITY_QUESTION || state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, username);
            bundle.putString(SmartLockActivity.PASSWORD, password);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, 200);
    }

}
