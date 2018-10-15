package com.tokopedia.session.register.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.analytics.SessionTrackingUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.customView.TextDrawable;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.WebViewLoginFragment;
import com.tokopedia.session.addname.AddNameActivity;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.login.loginemail.view.activity.ForbiddenActivity;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;
import com.tokopedia.session.register.registerphonenumber.view.activity.AddNameRegisterPhoneActivity;
import com.tokopedia.session.register.registerphonenumber.view.activity.RegisterPhoneNumberActivity;
import com.tokopedia.session.register.registerphonenumber.view.activity.WelcomePageActivity;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.register.view.customview.PartialRegisterInputView;
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
    private static final int REQUEST_VERIFY_PHONE = 105;
    private static final int REQUEST_WELCOME_PAGE = 106;
    private static final int REQUEST_ADD_NAME_REGISTER_PHONE = 107;
    private static final int REQUEST_VERIFY_PHONE_TOKOCASH = 108;
    private static final int REQUEST_CHOOSE_ACCOUNT = 109;
    private static final int REQUEST_NO_TOKOCASH_ACCOUNT = 110;
    private static final int REQUEST_ADD_NAME = 111;


    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "phonenumber";
    private static final String REMOTE_CONFIG_SHOW_REGISTER_PHONE_NUMBER =
            "mainapp_show_register_phone_number";
    private static final String COLOR_WHITE = "#FFFFFF";

    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    private TextView optionTitle;
    private PartialRegisterInputView partialRegisterInputView;
    private LinearLayout registerContainer, llLayout;
    private LoginTextView registerButton, registerPhoneNumberButton;
    private TextView loginButton;
    private ScrollView container;
    private RelativeLayout progressBar;
    private RegisterAnalytics analytics;

    private String socmedMethod = "";
    private String email = "";
    private String phoneNumber = "";

    @Inject
    RegisterInitialPresenter presenter;

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
        analytics = RegisterAnalytics.initAnalytics(getActivity());
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        if (savedInstanceState != null && savedInstanceState.containsKey(PHONE_NUMBER)) {
            phoneNumber = savedInstanceState.getString(PHONE_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_register_initial, parent, false);
        optionTitle = (TextView) view.findViewById(R.id.register_option_title);
        partialRegisterInputView = (PartialRegisterInputView) view.findViewById(R.id
                .register_input_view);
        registerContainer = (LinearLayout) view.findViewById(R.id.register_container);
        registerButton = (LoginTextView) view.findViewById(R.id.register);
        registerPhoneNumberButton = (LoginTextView) view.findViewById(R.id.register_phone_number);
        loginButton = (TextView) view.findViewById(R.id.login_button);
        container = (ScrollView) view.findViewById(R.id.container);
        progressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);
        llLayout = (LinearLayout) view.findViewById(R.id.ll_layout);
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
            drawable.setText(getResources().getString(R.string.login));
            drawable.setTextColor(getResources().getColor(R.color.colorGreen));
            drawable.setTextSize(14);
        }
        return drawable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_register) {
            goToLoginPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        presenter.getProvider();
        partialRegisterInputView.setPresenter(presenter);
    }

    protected void prepareView(View view) {
        UserAuthenticationAnalytics.setActiveRegister();
        registerButton.setVisibility(View.GONE);
        registerPhoneNumberButton.setVisibility(View.GONE);
        partialRegisterInputView.setVisibility(View.GONE);

        if (!GlobalConfig.isSellerApp()) {
            optionTitle.setText(R.string.register_option_title);
            optionTitle.setTypeface(Typeface.DEFAULT);
            optionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }

        registerButton.setColor(Color.WHITE);
        registerButton.setBorderColor(MethodChecker.getColor(getActivity(), R.color.black_38));
        registerButton.setRoundCorner(10);
        registerButton.setImageResource(R.drawable.ic_email_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterEmail());
                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
                goToRegisterEmailPage();

            }
        });

        if (GlobalConfig.isSellerApp()) {
            registerButton.setVisibility(View.VISIBLE);
        } else {
            partialRegisterInputView.setVisibility(View.VISIBLE);
        }
        registerPhoneNumberButton.setColor(Color.WHITE);
        registerPhoneNumberButton.setBorderColor(MethodChecker.getColor(getActivity(), R.color
                .black_38));
        registerPhoneNumberButton.setRoundCorner(10);
        registerPhoneNumberButton.setImageResource(R.drawable.ic_phone);
        registerPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                Intent intent = RegisterPhoneNumberActivity.getCallingIntent(getActivity());
                startActivityForResult(intent, REQUEST_REGISTER_PHONE_NUMBER);
            }
        });
        String sourceString = getActivity().getResources().getString(R.string
                .span_already_have_tokopedia_account);

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
                                  ds.setTypeface(Typeface.create("sans-serif-medium", Typeface
                                          .NORMAL));
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                , 0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    protected void setViewListener() {
        loginButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
                goToLoginPage();
            }
        });
    }

    @Override
    public void goToLoginPage() {
        analytics.eventClickOnLogin();

        Intent intent = LoginActivity.getCallingIntent(getActivity());
        startActivity(intent);
    }

    @Override
    public void goToRegisterEmailPage() {
        showProgressBar();
        Intent intent = RegisterEmailActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_REGISTER_EMAIL);
    }

    @Override
    public void goToRegisterEmailPageWithEmail(String email) {
        showProgressBar();
        Intent intent = RegisterEmailActivity.getCallingIntentWithEmail(getActivity(), email);
        startActivityForResult(intent, REQUEST_REGISTER_EMAIL);
    }

    @Override
    public void goToVerificationPhoneRegister(String phone) {
        Intent intent = VerificationActivity.getCallingIntent(
                getActivity(),
                phone,
                RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                true,
                RequestOtpUseCase.MODE_SMS
        );
        startActivityForResult(intent, REQUEST_VERIFY_PHONE);
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
        } else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity
                .RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity
                .RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
            sessionHandler.clearToken();
        } else if (requestCode == REQUEST_REGISTER_PHONE_NUMBER && resultCode == Activity
                .RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessRegisterSosmed(socmedMethod));

            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity
                .RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_SECURITY_QUESTION && resultCode == Activity
                .RESULT_CANCELED) {
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else if (requestCode == REQUEST_VERIFY_PHONE && resultCode == Activity.RESULT_OK) {
            startActivityForResult(AddNameRegisterPhoneActivity.newInstance(getActivity(), phoneNumber),
                    REQUEST_ADD_NAME_REGISTER_PHONE);
        } else if (requestCode == REQUEST_ADD_NAME_REGISTER_PHONE && resultCode == Activity.RESULT_OK) {
            startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                    REQUEST_WELCOME_PAGE);
        } else if (requestCode == REQUEST_WELCOME_PAGE) {
            if (resultCode == Activity.RESULT_OK) {
                goToProfileCompletionPage();
            } else {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        } else if (requestCode == REQUEST_VERIFY_PHONE_TOKOCASH && resultCode == Activity
                .RESULT_OK) {
            ChooseTokoCashAccountViewModel chooseTokoCashAccountViewModel = getChooseAccountData
                    (data);
            if (chooseTokoCashAccountViewModel != null && !chooseTokoCashAccountViewModel
                    .getListAccount().isEmpty()) {
                goToChooseAccountPage(chooseTokoCashAccountViewModel);
            } else {
                goToNoTokocashAccountPage(phoneNumber);
            }
        } else if (requestCode == REQUEST_CHOOSE_ACCOUNT
                && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_OK) {
            startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                    REQUEST_WELCOME_PAGE);
        } else if (requestCode == REQUEST_ADD_NAME && resultCode == Activity.RESULT_CANCELED) {
            sessionHandler.clearUserData(getActivity());
            dismissProgressBar();
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
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

    private void goToProfileCompletionPage() {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        Intent parentIntent = ((TkpdCoreRouter) getActivity().getApplicationContext())
                .getHomeIntent(getActivity());
        parentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent childIntent = new Intent(getActivity(), ProfileCompletionActivity.class);
        stackBuilder.addNextIntent(parentIntent);
        stackBuilder.addNextIntent(childIntent);
        getActivity().startActivities(stackBuilder.getIntents());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());

        layoutParams.setMargins(0, topMargin, 0, 0);

        for (int i = 0; i < listProvider.size(); i++) {
            DiscoverItemViewModel item = listProvider.get(i);
            if (!item.getId().equals(PHONE_NUMBER)) {
                LoginTextView loginTextView = new LoginTextView(getActivity()
                        , MethodChecker.getColor(getActivity(), R.color.white));
                loginTextView.setText(item.getName());
                loginTextView.setBorderColor(MethodChecker.getColor(getActivity(), R.color
                        .black_38));
                loginTextView.setImage(item.getImage());
                loginTextView.setRoundCorner(10);

                setDiscoverOnClickListener(item, loginTextView);

                if (registerContainer != null) {
                    registerContainer.addView(loginTextView, registerContainer.getChildCount(),
                            layoutParams);
                }
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
        SessionTrackingUtils.registerPageClickFacebook(com.tokopedia.core.analytics
                .AppEventTracking.GTMCacheValue.FACEBOOK);

    }

    private void onRegisterGooglelick() {
        UnifyTracking.eventTracking(LoginAnalytics.getEventClickRegisterGoogle());
        UnifyTracking.eventMoRegistrationStart(
                AppEventTracking.GTMCacheValue.GMAIL);

        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
        SessionTrackingUtils.registerPageClickGoogle(GoogleSignInActivity.class.getName());

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
        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessRegisterSosmed(socmedMethod));
        startActivityForResult(WelcomePageActivity.newInstance(getActivity()),
                REQUEST_WELCOME_PAGE);
    }

    @Override
    public void onGoToCreatePasswordPage(GetUserInfoDomainData userInfoDomainData,
                                         String methodName) {
        socmedMethod = methodName;
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
    public void onGoToAddName(GetUserInfoDomainData getUserInfoDomainData) {
        Intent intent = AddNameActivity.newInstance(getActivity());
        startActivityForResult(intent, REQUEST_ADD_NAME);
    }

    @Override
    public void onGoToSecurityQuestion(SecurityDomain securityDomain, String fullName, String
            email, String phone) {

        Intent intent = VerificationActivity.getShowChooseVerificationMethodIntent(
                getActivity(), RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION, phone, email);
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
    public void showRegisteredEmailDialog(String email) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.email_already_registered));
        dialog.setDesc(
                String.format(getResources().getString(
                        R.string.email_already_registered_info), email));
        dialog.setBtnOk(getString(R.string.already_registered_yes));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventProceedEmailAlreadyRegistered();
                dialog.dismiss();
                startActivity(LoginActivity.getIntentLoginFromRegister(getActivity(), email));
                getActivity().finish();
            }
        });
        dialog.setBtnCancel(getString(R.string.already_registered_no));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.eventCancelEmailAlreadyRegistered();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showRegisteredPhoneDialog(String phone) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.phone_number_already_registered));
        dialog.setDesc(
                String.format(getResources().getString(
                        R.string.reigster_page_phone_number_already_registered_info), phone));
        dialog.setBtnOk(getString(R.string.already_registered_yes));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                phoneNumber = phone;
                goToVerifyAccountPage(phoneNumber);
            }
        });
        dialog.setBtnCancel(getString(R.string.already_registered_no));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void goToVerifyAccountPage(String phoneNumber) {
        startActivityForResult(com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity
                        .getLoginTokoCashVerificationIntent(
                                getActivity(),
                                phoneNumber,
                                getListVerificationMethod(phoneNumber)),
                REQUEST_VERIFY_PHONE_TOKOCASH);
    }

    private ArrayList<MethodItem> getListVerificationMethod(String phoneNumber) {
        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity.TYPE_SMS,
                R.drawable.ic_verification_sms,
                MethodItem.getSmsMethodText(phoneNumber)
        ));
        list.add(new MethodItem(
                com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity.TYPE_PHONE_CALL,
                R.drawable.ic_verification_call,
                MethodItem.getCallMethodText(phoneNumber)
        ));
        return list;
    }

    private void goToNoTokocashAccountPage(String phoneNumber) {
        startActivityForResult(NotConnectedTokocashActivity.getNoTokocashAccountIntent(
                getActivity(),
                phoneNumber),
                REQUEST_NO_TOKOCASH_ACCOUNT);
    }

    private void goToChooseAccountPage(ChooseTokoCashAccountViewModel data) {
        startActivityForResult(ChooseTokocashAccountActivity.getCallingIntent(
                getActivity(),
                data),
                REQUEST_CHOOSE_ACCOUNT);
    }

    private ChooseTokoCashAccountViewModel getChooseAccountData(Intent data) {
        return data.getParcelableExtra(ChooseTokocashAccountActivity.ARGS_DATA);
    }

    @Override
    public void showProceedWithPhoneDialog(String phone) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(phone);
        dialog.setDesc(getResources().getString(R.string.phone_number_not_registered_info));
        dialog.setBtnOk(getString(R.string.proceed_with_phone_number));
        dialog.setOnOkClickListener(v -> {
            analytics.eventProceedRegisterWithPhoneNumber();
            dialog.dismiss();
            goToVerificationPhoneRegister(phone);
        });
        dialog.setBtnCancel(getString(R.string.already_registered_no));
        dialog.setOnCancelClickListener(v -> {
            analytics.eventCancelRegisterWithPhoneNumber();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onErrorValidateRegister(String message) {
        partialRegisterInputView.onErrorValidate(message);
        phoneNumber = "";
    }

    @Override
    public void onErrorConnectionSnackbar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void setTempPhoneNumber(String maskedPhoneNumber) {
        //use masked phone number form backend when needed
        //we need unmasked phone number (without dash) to be provided to backend
        this.phoneNumber = partialRegisterInputView.getTextValue();
    }

    @Override
    public GetFacebookCredentialSubscriber.GetFacebookCredentialListener
    getFacebookCredentialListener() {
        return new GetFacebookCredentialSubscriber.GetFacebookCredentialListener() {
            @Override
            public void onErrorGetFacebookCredential(String errorMessage) {
                if (isAdded()) {
                    NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
                }
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(PHONE_NUMBER, phoneNumber);
        super.onSaveInstanceState(outState);
    }
}
