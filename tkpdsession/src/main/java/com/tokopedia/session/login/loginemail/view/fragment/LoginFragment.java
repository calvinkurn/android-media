package com.tokopedia.session.login.loginemail.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.WebViewLoginFragment;
import com.tokopedia.session.activation.view.activity.ActivationActivity;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.login.loginemail.view.activity.ForbiddenActivity;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.login.loginemail.view.presenter.LoginPresenter;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.activity.RegisterInitialActivity;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT;
import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginFragment extends BaseDaggerFragment
        implements Login.View {

    private static final String COLOR_WHITE = "#FFFFFF";
    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "tokocash";

    private static final int REQUEST_SMART_LOCK = 101;
    private static final int REQUEST_SAVE_SMART_LOCK = 102;
    private static final int REQUEST_LOGIN_WEBVIEW = 103;
    private static final int REQUEST_SECURITY_QUESTION = 104;
    private static final int REQUEST_LOGIN_PHONE_NUMBER = 105;
    private static final int REQUESTS_CREATE_PASSWORD = 106;
    private static final int REQUEST_ACTIVATE_ACCOUNT = 107;
    private static final int REQUEST_VERIFY_PHONE = 108;


    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    public static final String IS_AUTO_LOGIN = "auto_login";
    public static final String AUTO_LOGIN_METHOD = "method";

    public static final String AUTO_LOGIN_EMAIL = "email";
    public static final String AUTO_LOGIN_PASS = "pw";
    private static final int MINIMAL_HEIGHT = 1200;

    AutoCompleteTextView emailEditText;
    TextInputEditText passwordEditText;
    ScrollView loginView;
    View loadingView;
    View rootView;
    TextView forgotPass;
    LinearLayout loginLayout;
    LinearLayout loginButtonsContainer;
    TextView loginButton;
    TkpdHintTextInputLayout wrapperEmail;
    TkpdHintTextInputLayout wrapperPassword;
    ImageView loadMoreFab;

    ArrayAdapter<String> autoCompleteAdapter;
    CallbackManager callbackManager;

    @Inject
    LoginPresenter presenter;

    @Inject
    GlobalCacheManager cacheManager;

    @Inject
    SessionHandler sessionHandler;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_LOGIN;
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

    public void initOuterInjector(SessionModule sessionModule){
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .sessionModule(sessionModule)
                        .build();
        daggerSessionComponent.inject(this);

        presenter.attachView(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionHandler != null &&
                sessionHandler.isV4Login()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, R.id.action_register, 0, "");
        MenuItem menuItem = menu.findItem(R.id.action_register);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        if (getDraw() != null) {
            menuItem.setIcon(getDraw());
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private Drawable getDraw() {
        TextDrawable drawable = null;
        if (getActivity() != null) {
            drawable = new TextDrawable(getActivity());
            drawable.setText(getResources().getString(R.string.register));
            drawable.setTextColor(R.color.black_70b);
        }
        return drawable;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_register) {
            goToRegisterInitial();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserAuthenticationAnalytics.setActiveLogin();
        callbackManager = CallbackManager.Factory.create();
        sessionHandler.clearToken();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_login, parent, false);
        rootView = view.findViewById(R.id.root);
        emailEditText = view.findViewById(R.id.email_auto);
        passwordEditText = view.findViewById(R.id.password);
        loginView = view.findViewById(R.id.login_form);
        loadingView = view.findViewById(R.id.login_status);
        forgotPass = view.findViewById(R.id.forgot_pass);
        loginLayout = view.findViewById(R.id.login_layout);
        loginButton = view.findViewById(R.id.accounts_sign_in);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        loadMoreFab = view.findViewById(R.id.btn_load_more);
        loginButtonsContainer = view.findViewById(R.id.login_buttons_container);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {

        passwordEditText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == R.id.ime_login || id == EditorInfo.IME_NULL) {
                            presenter.login(emailEditText.getText().toString().trim(),
                                    passwordEditText.getText().toString());
                            return true;
                        }

                        return false;
                    }
                });

        passwordEditText.addTextChangedListener(watcher(wrapperPassword));


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
                presenter.saveLoginEmail(emailEditText.getText().toString());
                presenter.login(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString());
                UnifyTracking.eventCTAAction();
            }
        });

        emailEditText.addTextChangedListener(watcher(wrapperEmail));

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ForgotPasswordActivity.getCallingIntent(getActivity(), emailEditText.getText()
                        .toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });

        loginView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isLastItem()) {
                    loadMoreFab.setVisibility(View.GONE);
                } else {
                    loadMoreFab.setVisibility(View.VISIBLE);
                }

            }
        });

        loadMoreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginView.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        autoCompleteAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                presenter.getLoginIdList());
        emailEditText.setAdapter(autoCompleteAdapter);

        presenter.discoverLogin();

        if (getArguments().getBoolean(IS_AUTO_LOGIN, false)) {
            switch (getArguments().getInt(AUTO_LOGIN_METHOD)) {
                case LoginActivity.METHOD_FACEBOOK:
                    onLoginFacebookClick();
                    break;
                case LoginActivity.METHOD_GOOGLE:
                    onLoginGoogleClick();
                    break;
                case LoginActivity.METHOD_WEBVIEW:
                    if (!TextUtils.isEmpty(getArguments().getString(LoginActivity
                            .AUTO_WEBVIEW_NAME, ""))
                            && !TextUtils.isEmpty(getArguments().getString(LoginActivity
                            .AUTO_WEBVIEW_URL, ""))) {
                        onLoginWebviewClick(getArguments().getString(LoginActivity.AUTO_WEBVIEW_NAME,
                                ""),
                                getArguments().getString(LoginActivity.AUTO_WEBVIEW_URL,
                                        ""));
                    }
                    break;
                case LoginActivity.METHOD_EMAIL:
                    String email = getArguments().getString(AUTO_LOGIN_EMAIL, "");
                    String pw = getArguments().getString(AUTO_LOGIN_PASS, "");
                    emailEditText.setText(email);
                    passwordEditText.setText(pw);
                    presenter.login(email, pw);
                    break;
                default:
                    showSmartLock();
                    break;
            }
        } else {
            showSmartLock();
        }
    }

    private void showSmartLock() {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, SmartLockActivity.RC_READ);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SMART_LOCK);
    }


    private void goToRegisterInitial() {
        UnifyTracking.eventTracking(LoginAnalytics.goToRegisterFromLogin());
        Intent intent = RegisterInitialActivity.getCallingIntent(getActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void resetError() {
        setWrapperError(wrapperEmail, null);
        setWrapperError(wrapperPassword, null);
    }

    @Override
    public void showLoadingLogin() {
        showLoading(true);
    }

    private void showLoading(final boolean isLoading) {
        int shortAnimTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        loadingView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loadingView != null) {
                            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                        }
                    }
                });

        loginView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loginView != null) {
                            loginView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                        }
                    }
                });

    }

    @Override
    public void dismissLoadingLogin() {
        showLoading(false);
    }

    @Override
    public void showErrorPassword(int resId) {
        setWrapperError(wrapperPassword, getString(resId));
        passwordEditText.requestFocus();
        UnifyTracking.eventLoginError(AppEventTracking.EventLabel.PASSWORD);
    }

    @Override
    public void showErrorEmail(int resId) {
        setWrapperError(wrapperEmail, getString(resId));
        emailEditText.requestFocus();
        UnifyTracking.eventLoginError(AppEventTracking.EventLabel.EMAIL);
    }

    private void saveSmartLock(int state, String email, String password) {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, state);
        if (state == SmartLockActivity.RC_SAVE_SECURITY_QUESTION || state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, email);
            bundle.putString(SmartLockActivity.PASSWORD, password);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SAVE_SMART_LOCK);
    }

    @Override
    public void onSuccessLogin() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorLogin(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setAutoCompleteAdapter(ArrayList<String> listId) {
        autoCompleteAdapter.clear();
        autoCompleteAdapter.addAll(listId);
        autoCompleteAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingDiscover() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = loginLayout.getChildCount() - 1;
        if (loginLayout != null && !(loginLayout.getChildAt(lastPos) instanceof ProgressBar)) {
            loginLayout.addView(pb, loginLayout.getChildCount());
        }
    }

    @Override
    public void dismissLoadingDiscover() {
        int lastPos = loginLayout.getChildCount() - 1;
        if (loginLayout != null && loginLayout.getChildAt(lastPos) instanceof ProgressBar) {
            loginLayout.removeViewAt(loginLayout.getChildCount() - 1);
        }
    }

    @Override
    public void onErrorDiscoverLogin(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.discoverLogin();
            }
        }).showRetrySnackbar();
        loginButton.setEnabled(false);
    }

    @Override
    public void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> listProvider) {
        loginButton.setEnabled(true);
        listProvider.add(2, getLoginPhoneNumberBean());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 15);
        loginButtonsContainer.removeAllViews();
        for (int i = 0; i < listProvider.size(); i++) {
            int colorInt = Color.parseColor(COLOR_WHITE);
            LoginTextView tv = new LoginTextView(getActivity(), colorInt);
            tv.setTag(listProvider.get(i).getId());
            tv.setText(listProvider.get(i).getName());
            if (!TextUtils.isEmpty(listProvider.get(i).getImage())) {
                tv.setImage(listProvider.get(i).getImage());
            } else if (listProvider.get(i).getImageResource() != 0) {
                tv.setImageResource(listProvider.get(i).getImageResource());
            }
            tv.setRoundCorner(10);

            setDiscoverListener(listProvider.get(i), tv);
            if (loginButtonsContainer != null) {
                loginButtonsContainer.addView(tv, loginButtonsContainer.getChildCount(), layoutParams);
            }
        }

        enableArrow();
    }

    @Override
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener getFacebookCredentialListener() {
        return new GetFacebookCredentialSubscriber.GetFacebookCredentialListener() {
            @Override
            public void onErrorGetFacebookCredential(String errorMessage) {
                NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            }

            @Override
            public void onSuccessGetFacebookCredential(AccessToken accessToken, String email) {
                presenter.loginFacebook(accessToken, email);
            }
        };
    }

    @Override
    public void onGoToCreatePasswordPage(GetUserInfoDomainData userInfoDomainData) {
        Intent intent = CreatePasswordActivity.getCallingIntent(getActivity(),
                new CreatePasswordViewModel(
                        userInfoDomainData.getEmail(),
                        userInfoDomainData.getFullName(),
                        userInfoDomainData.getBdayYear(),
                        userInfoDomainData.getBdayMonth(),
                        userInfoDomainData.getBdayDay(),
                        userInfoDomainData.getCreatePasswordList(),
                        String.valueOf(userInfoDomainData.getUserId())));
        startActivityForResult(intent, REQUESTS_CREATE_PASSWORD);
    }

    @Override
    public void onGoToPhoneVerification() {
        startActivityForResult(
                PhoneVerificationActivationActivity.getCallingIntent(getActivity()),
                REQUEST_VERIFY_PHONE);
    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName,
                                       String email, String phone) {

        InterruptVerificationViewModel interruptVerificationViewModel;
        if (securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE) {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultSmsInterruptPage(phone);
        } else {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultEmailInterruptPage(email);
        }

        VerificationPassModel passModel = new
                VerificationPassModel(phone, email,
                RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                interruptVerificationViewModel,
                securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE
        );
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = VerificationActivity.getSecurityQuestionVerificationIntent(getActivity(),
                securityDomain.getUserCheckSecurity2());
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION);

    }

    @Override
    public void setSmartLock() {
        saveSmartLock(SmartLockActivity.RC_SAVE_SECURITY_QUESTION,
                emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    @Override
    public void resetToken() {
        presenter.resetToken();
    }

    @Override
    public void onErrorLogin(String errorMessage, int codeError) {
        onErrorLogin(errorMessage + getString(R.string.code_error) + " " + codeError);
    }

    @Override
    public void onGoToActivationPage(String email) {
        Intent intent = ActivationActivity.getCallingIntent(getActivity(),
                email, passwordEditText.getText().toString());
        startActivityForResult(intent, REQUEST_ACTIVATE_ACCOUNT);
    }

    @Override
    public void onSuccessLoginEmail() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessLoginEmail());
        TrackingUtils.setMoEUserAttributesLogin(
                sessionHandler.getLoginID(),
                sessionHandler.getLoginName(),
                sessionHandler.getEmail(),
                sessionHandler.getPhoneNumber(),
                sessionHandler.isGoldMerchant(MainApplication.getAppContext()),
                sessionHandler.getShopName(),
                sessionHandler.getShopID(),
                !TextUtils.isEmpty(sessionHandler.getShopID()),
                LoginAnalytics.Label.EMAIL
        );

        BranchSdkUtils.sendLoginEvent(getActivity());

        onSuccessLogin();
    }

    @Override
    public void onSuccessLoginSosmed(String loginMethod) {
        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessLoginSosmed(loginMethod));
        TrackingUtils.setMoEUserAttributesLogin(
                sessionHandler.getLoginID(),
                sessionHandler.getLoginName(),
                sessionHandler.getEmail(),
                sessionHandler.getPhoneNumber(),
                sessionHandler.isGoldMerchant(MainApplication.getAppContext()),
                sessionHandler.getShopName(),
                sessionHandler.getShopID(),
                !TextUtils.isEmpty(sessionHandler.getShopID()),
                loginMethod
        );

        BranchSdkUtils.sendLoginEvent(getActivity());
        onSuccessLogin();
    }

    @Override
    public void disableArrow() {
        loadMoreFab.setVisibility(View.GONE);
    }

    @Override
    public void enableArrow() {
        ViewTreeObserver observer = loginView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int viewHeight = loginView.getMeasuredHeight();
                int contentHeight = loginView.getChildAt(0).getHeight();
                if (viewHeight - contentHeight < 0
                        && !isLastItem()) {
                    loadMoreFab.setVisibility(View.VISIBLE);
                } else {
                    loadMoreFab.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
    }

    private boolean isLastItem() {
        return loginView.getChildAt(0).getBottom() <= (loginView.getHeight() + loginView
                .getScrollY());
    }

    private void setDiscoverListener(final DiscoverItemViewModel discoverItemViewModel,
                                     LoginTextView tv) {
        if (discoverItemViewModel.getId().equalsIgnoreCase(FACEBOOK)) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginFacebookClick();
                }
            });
        } else if (discoverItemViewModel.getId().equalsIgnoreCase(GPLUS)) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginGoogleClick();
                }
            });
        } else if (discoverItemViewModel.getId().equalsIgnoreCase(PHONE_NUMBER)) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginPhoneNumberClick();
                }
            });
        } else {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginWebviewClick(discoverItemViewModel.getName(),
                            discoverItemViewModel.getUrl());
                }
            });
        }
    }

    private void onLoginWebviewClick(String name, String url) {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickLoginWebview(name));
        WebViewLoginFragment newFragment = WebViewLoginFragment
                .createInstance(url, name);
        newFragment.setTargetFragment(this, REQUEST_LOGIN_WEBVIEW);
        newFragment.show(getFragmentManager().beginTransaction(), "dialog");
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void onLoginPhoneNumberClick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickLoginPhoneNumber());
        Intent intent = LoginPhoneNumberActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_LOGIN_PHONE_NUMBER);
    }

    private void onLoginGoogleClick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickLoginGoogle());
        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    private void onLoginFacebookClick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickLoginFacebook());
        presenter.getFacebookCredential(this, callbackManager);
    }

    private DiscoverItemViewModel getLoginPhoneNumberBean() {
        DiscoverItemViewModel phoneNumberBean = new DiscoverItemViewModel(
                PHONE_NUMBER,
                getString(com.tokopedia.session.R.string.phone_number),
                "",
                "",
                COLOR_WHITE
        );
        phoneNumberBean.setImageResource(R.drawable.ic_phone);
        return phoneNumberBean;
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    private TextWatcher watcher(final TkpdHintTextInputLayout wrapper) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SMART_LOCK
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getExtras() != null
                && data.getExtras().getString(SmartLockActivity.USERNAME) != null
                && data.getExtras().getString(SmartLockActivity.PASSWORD) != null) {
            emailEditText.setText(data.getExtras().getString(SmartLockActivity.USERNAME));
            passwordEditText.setText(data.getExtras().getString(SmartLockActivity.PASSWORD));
            presenter.login(data.getExtras().getString(SmartLockActivity.USERNAME),
                    data.getExtras().getString(SmartLockActivity.PASSWORD));
        } else if (requestCode == RC_SIGN_IN_GOOGLE && data != null) {
            GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
            String email = googleSignInAccount.getEmail();
            String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);
            presenter.loginGoogle(accessToken, email);
        } else if (requestCode == REQUEST_LOGIN_WEBVIEW && resultCode == Activity.RESULT_OK) {
            presenter.loginWebview(data);
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN_PHONE_NUMBER && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUESTS_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_ACTIVATE_ACCOUNT && resultCode == Activity.RESULT_CANCELED) {
            dismissLoadingLogin();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_VERIFY_PHONE) {
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
