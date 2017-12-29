package com.tokopedia.session.login.loginemail.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.activity.ActivationActivity;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.google.GoogleSignInActivity;
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
import com.tokopedia.session.session.fragment.WebViewLoginFragment;

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

    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    public static final String IS_AUTO_LOGIN = "auto_login";
    public static final String AUTO_LOGIN_METHOD = "method";


    AutoCompleteTextView emailEditText;
    TextInputEditText passwordEditText;
    View loginView;
    View loadingView;
    TextView registerButton;
    TextView forgotPass;
    LinearLayout loginLayout;
    TextView loginButton;
    TextInputLayout wrapperEmail;
    TextInputLayout wrapperPassword;

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

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
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
        View view = inflater.inflate(R.layout.fragment_login, parent, false);
        emailEditText = view.findViewById(R.id.email_auto);
        passwordEditText = view.findViewById(R.id.password);
        loginView = view.findViewById(R.id.login_form);
        loadingView = view.findViewById(R.id.login_status);
        registerButton = view.findViewById(R.id.register_button);
        forgotPass = view.findViewById(R.id.forgot_pass);
        loginLayout = view.findViewById(R.id.login_layout);
        loginButton = view.findViewById(R.id.accounts_sign_in);
        wrapperEmail = view.findViewById(R.id.wrapper_email);
        wrapperPassword = view.findViewById(R.id.wrapper_password);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {

        String sourceString = getString(com.tokopedia.core.R.string.register_text_login);

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
                , sourceString.indexOf(getString(R.string.register))
                , sourceString.length()
                , 0);

        registerButton.setText(spannable, TextView.BufferType.SPANNABLE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventRegisterThroughLogin();
                goToRegisterInitial();
            }
        });


        passwordEditText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int id,
                                                  KeyEvent keyEvent) {
                        if (id == com.tokopedia.core.R.id.login || id == EditorInfo.IME_NULL) {
                            presenter.login(emailEditText.getText().toString().trim(),
                                    passwordEditText.getText().toString());
                            return true;
                        } else {
                            return false;
                        }
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
                getActivity().finish();
                startActivity(ForgotPasswordActivity.getCallingIntent(getActivity(), emailEditText.getText()
                        .toString()));
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

        if (getArguments().getBoolean(IS_AUTO_LOGIN)) {
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
        startActivity(RegisterInitialActivity.getCallingIntent(getActivity()));
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
                        if (loadingView != null)
                            loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                });

        loginView.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (loginView != null)
                            loginView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
                    }
                });

        registerButton.animate().setDuration(shortAnimTime)
                .alpha(isLoading ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //[END] save progress for rotation
                        if (registerButton != null)
                            registerButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
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
        if (loginLayout != null && !(loginLayout.getChildAt(lastPos) instanceof ProgressBar))
            loginLayout.addView(pb, loginLayout.getChildCount());
    }

    @Override
    public void dismissLoadingDiscover() {
        int lastPos = loginLayout.getChildCount() - 1;
        if (loginLayout != null && loginLayout.getChildAt(lastPos) instanceof ProgressBar)
            loginLayout.removeViewAt(loginLayout.getChildCount() - 1);
    }

    @Override
    public void onErrorDiscoverLogin(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.discoverLogin();
            }
        });
        loginButton.setEnabled(false);
    }

    @Override
    public void onSuccessDiscoverLogin(ArrayList<DiscoverItemViewModel> listProvider) {
        loginButton.setEnabled(true);
        listProvider.add(2, getLoginPhoneNumberBean());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 15);
        for (int i = 0; i < listProvider.size(); i++) {
            String color = listProvider.get(i).getColor();
            int colorInt;
            if (color == null) {
                colorInt = Color.parseColor(COLOR_WHITE);
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

            setDiscoverListener(listProvider.get(i), tv);
            if (loginLayout != null) {
                loginLayout.addView(tv, loginLayout.getChildCount(), layoutParams);
            }
        }
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
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onGoToPhoneVerification() {
        getActivity().setResult(Activity.RESULT_OK);
        startActivity(
                PhoneVerificationActivationActivity.getCallingIntent(getActivity()));
        getActivity().finish();
    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName,
                                       String email, String phone) {

        VerificationPassModel passModel = new VerificationPassModel(phone, email,
                getListAvailableMethod(securityDomain, phone), RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION);
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();


        Intent intent = VerificationActivity.getSecurityQuestionVerificationIntent(getActivity(),
                securityDomain.getUserCheckSecurity2());
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        getActivity().finish();

    }

    private ArrayList<MethodItem> getListAvailableMethod(SecurityDomain securityDomain, String phone) {
        ArrayList<MethodItem> list = new ArrayList<>();
        if (securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE) {
            list.add(new MethodItem(
                    VerificationActivity.TYPE_SMS,
                    R.drawable.ic_verification_sms,
                    MethodItem.getSmsMethodText(phone)
            ));
            list.add(new MethodItem(
                    VerificationActivity.TYPE_PHONE_CALL,
                    R.drawable.ic_verification_call,
                    MethodItem.getCallMethodText(phone)
            ));
        }
        return list;
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
                email);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
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
        UnifyTracking.eventCTAAction(name);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(name);

        WebViewLoginFragment newFragment = WebViewLoginFragment
                .createInstance(url);
        newFragment.setTargetFragment(this, REQUEST_LOGIN_WEBVIEW);
        newFragment.show(getFragmentManager().beginTransaction(), "dialog");
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void onLoginPhoneNumberClick() {
        UnifyTracking.eventCTAAction(PHONE_NUMBER);
        Intent intent = LoginPhoneNumberActivity.getCallingIntent(getActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    private void onLoginGoogleClick() {
        UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.GMAIL);

        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    private void onLoginFacebookClick() {
        UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(AppEventTracking.GTMCacheValue.FACEBOOK);
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

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        } else if (requestCode == REQUEST_SMART_LOCK && resultCode == SmartLockActivity.RESULT_CANCELED) {
            //TODO : FIX SMART LOCK ERROR STATE
//            onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode
//                    .SMART_LOCK_FAILED_TO_GET_CREDENTIALS));
        } else if (requestCode == RC_SIGN_IN_GOOGLE && data != null) {
            GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
            String email = googleSignInAccount.getEmail();
            String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);
            presenter.loginGoogle(accessToken, email);
        } else if (requestCode == REQUEST_LOGIN_WEBVIEW && resultCode == Activity.RESULT_OK) {
            presenter.loginWebview(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
