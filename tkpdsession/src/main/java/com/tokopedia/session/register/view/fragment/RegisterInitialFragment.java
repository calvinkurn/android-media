package com.tokopedia.session.register.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.WebViewLoginFragment;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.login.loginemail.view.activity.ForbiddenActivity;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.register.registerphonenumber.view.activity.RegisterPhoneNumberActivity;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.register.view.presenter.RegisterInitialPresenter;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterInitialFragment extends BaseDaggerFragment
        implements RegisterInitial.View {

    private static final int REQUEST_REGISTER_WEBVIEW = 100;
    private static final int REQUEST_REGISTER_EMAIL = 101;
    private static final int REQUEST_CREATE_PASSWORD = 102;
    private static final int REQUEST_SECURITY_QUESTION = 103;
    private static final int REQUEST_REGISTER_PHONE_NUMBER = 104;

    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "phonenumber";
    private static final String REMOTE_CONFIG_SHOW_REGISTER_PHONE_NUMBER = "mainapp_show_register_phone_number";
    private static final String COLOR_WHITE = "#FFFFFF";

    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    LinearLayout registerContainer;
    LoginTextView registerButton, registerPhoneNumberButton;
    TextView loginButton;
    ScrollView container;
    RelativeLayout progressBar;

    @Inject
    RegisterInitialPresenter presenter;

    @Inject
    GlobalCacheManager cacheManager;

    @Inject
    SessionHandler sessionHandler;

    CallbackManager callbackManager;
    RemoteConfig remoteConfig;

    public static RegisterInitialFragment createInstance() {
        return new RegisterInitialFragment();
    }


    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
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
        callbackManager = CallbackManager.Factory.create();
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_initial, parent, false);

        registerContainer = (LinearLayout) view.findViewById(R.id.register_container);
        registerButton = (LoginTextView) view.findViewById(R.id.register);
        registerPhoneNumberButton = (LoginTextView) view.findViewById(R.id.register_phone_number);
        loginButton = (TextView) view.findViewById(R.id.login_button);
        container = (ScrollView) view.findViewById(R.id.container);
        progressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);
        prepareView(view);
        setViewListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        presenter.getProvider();
    }

    protected void prepareView(View view) {
        UserAuthenticationAnalytics.setActiveRegister();
        registerPhoneNumberButton.setVisibility(View.GONE);

        registerButton.setColor(Color.WHITE);
        registerButton.setBorderColor(R.color.black);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterEmail());
                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
                showProgressBar();
                Intent intent = RegisterEmailActivity.getCallingIntent(getActivity());
                startActivityForResult(intent, REQUEST_REGISTER_EMAIL);

            }
        });
        registerPhoneNumberButton.setColor(Color.WHITE);
        registerPhoneNumberButton.setBorderColor(R.color.black);
        registerPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterEmail());
//                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
                showProgressBar();
                Intent intent = RegisterPhoneNumberActivity.getCallingIntent(getActivity());
                startActivityForResult(intent, REQUEST_REGISTER_PHONE_NUMBER);

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
                                  ds.setColor(MethodChecker.getColor(
                                          getActivity(), R.color.tkpd_main_green
                                          )
                                  );
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                , 0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    protected void setViewListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent intent = LoginActivity.getCallingIntent(getActivity());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_REGISTER_WEBVIEW) {
            handleRegisterWebview(resultCode, data);
        } else if (requestCode == RC_SIGN_IN_GOOGLE && data != null) {
            String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);
            presenter.registerGoogle(accessToken);
        } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }  else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity.RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleRegisterWebview(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
        } else {
            presenter.registerWebview(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenTracking.screen(getScreenName());

        if (sessionHandler != null &&
                sessionHandler.isV4Login()) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void showLoadingDiscover() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = registerContainer.getChildCount() - 1;
        if (!(registerContainer.getChildAt(lastPos) instanceof ProgressBar)) {
            registerContainer.addView(pb, registerContainer.getChildCount());
        }
    }

    @Override
    public void onErrorDiscoverRegister(String errorMessage) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                errorMessage, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getProvider();
                    }
                }).showRetrySnackbar();
        loginButton.setEnabled(false);
    }

    @Override
    public void onSuccessDiscoverRegister(ArrayList<DiscoverItemViewModel> listProvider) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.btn_login_height));

        layoutParams.setMargins(0, 20, 0, 15);

        for (int i = 0; i < listProvider.size(); i++) {
            DiscoverItemViewModel item = listProvider.get(i);
            if (!item.getId().equals(PHONE_NUMBER)) {
                String color = item.getColor();
                int colorInt;
                if (color == null) {
                    colorInt = Color.parseColor(COLOR_WHITE);
                } else {
                    colorInt = Color.parseColor(color);
                }
                LoginTextView loginTextView = new LoginTextView(getActivity(), colorInt);
                loginTextView.setTextRegister(item.getName());
                loginTextView.setImage(item.getImage());
                loginTextView.setRoundCorner(10);

                setDiscoverOnClickListener(item, loginTextView);

                if (registerContainer != null) {
                    registerContainer.addView(loginTextView, registerContainer.getChildCount(), layoutParams);
                }
            } else if (!GlobalConfig.isSellerApp() && remoteConfig.getBoolean(REMOTE_CONFIG_SHOW_REGISTER_PHONE_NUMBER)) {
                registerPhoneNumberButton.setVisibility(View.VISIBLE);
                registerPhoneNumberButton.setImage(item.getImage());
            }
        }

    }

    private void setDiscoverOnClickListener(final DiscoverItemViewModel discoverItemViewModel,
                                            LoginTextView loginTextView) {

        switch (discoverItemViewModel.getId().toLowerCase()) {
            case FACEBOOK:
                loginTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRegisterFacebookClick();
                    }
                });
                break;
            case GPLUS:
                loginTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRegisterGooglelick();
                    }
                });
                break;
            default:
                loginTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRegisterWebviewClick(discoverItemViewModel);
                    }
                });
                break;
        }
    }

    private void onRegisterFacebookClick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterFacebook());
        UnifyTracking.eventMoRegistrationStart(
                com.tokopedia.core.analytics.AppEventTracking.GTMCacheValue.FACEBOOK);

        presenter.getFacebookCredential(this, callbackManager);


    }

    private void onRegisterGooglelick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterGoogle());
        UnifyTracking.eventMoRegistrationStart(
                AppEventTracking.GTMCacheValue.GMAIL);

        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);

    }

    private void onRegisterWebviewClick(DiscoverItemViewModel discoverItemViewModel) {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterWebview
                (discoverItemViewModel.getName()));
        UnifyTracking.eventMoRegistrationStart(
                AppEventTracking.GTMCacheValue.WEBVIEW);

        WebViewLoginFragment newFragment = WebViewLoginFragment.createInstance(
                discoverItemViewModel.getUrl(), discoverItemViewModel.getName());
        newFragment.setTargetFragment(RegisterInitialFragment.this, REQUEST_REGISTER_WEBVIEW);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        newFragment.show(fragmentTransaction, WebViewLoginFragment.class.getSimpleName());
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(discoverItemViewModel
                .getName());

    }


    @Override
    public void dismissLoadingDiscover() {
        int lastPos = registerContainer.getChildCount() - 1;
        if (registerContainer.getChildAt(lastPos) instanceof ProgressBar) {
            registerContainer.removeViewAt(registerContainer.getChildCount() - 1);
        }
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (container != null) {
            container.setVisibility(View.GONE);
        }
        if (loginButton != null) {
            loginButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (container != null) {
            container.setVisibility(View.VISIBLE);
        }
        if (loginButton != null) {
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorRegisterSosmed(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRegisterSosmed(String methodName) {
        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessRegisterSosmed(methodName));

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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
        startActivityForResult(intent, REQUEST_CREATE_PASSWORD);
    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String email, String phone) {

        InterruptVerificationViewModel interruptVerificationViewModel;
        if (securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE) {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultSmsInterruptPage(phone);
        } else {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultEmailInterruptPage(email);
        }

        VerificationPassModel passModel = new VerificationPassModel(phone, email,
                RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                interruptVerificationViewModel,
                securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE);
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
    public void onGoToPhoneVerification() {
        getActivity().setResult(Activity.RESULT_OK);
        startActivity(
                PhoneVerificationActivationActivity.getCallingIntent(getActivity()));
        getActivity().finish();
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
                presenter.registerFacebook(accessToken);
            }
        };
    }

    @Override
    public void onForbidden() {
        ForbiddenActivity.startActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
