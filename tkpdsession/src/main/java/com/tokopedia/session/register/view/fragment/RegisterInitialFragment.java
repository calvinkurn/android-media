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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.profile.model.GetUserInfoDomainData;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.di.DaggerSessionComponent;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.register.view.activity.CreatePasswordActivity;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.register.view.presenter.RegisterInitialPresenter;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;
import com.tokopedia.session.session.activity.Login;
import com.tokopedia.session.session.model.LoginModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT;
import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterInitialFragment extends BaseDaggerFragment
        implements RegisterInitial.View {

    private static final int REQUEST_LOGIN_WEBVIEW = 100;
    private static final int REQUEST_REGISTER_EMAIL = 101;
    private static final int REQUEST_LOGIN = 102;
    private static final int REQUEST_CREATE_PASSWORD = 103;

    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String COLOR_WHITE = "#FFFFFF";

    private static final String ARGS_MESSAGE = "message";

    LinearLayout linearLayout;
    LoginTextView registerButton;
    TextView loginButton;
    ScrollView container;
    RelativeLayout progressBar;

    @Inject
    RegisterInitialPresenter presenter;

    CallbackManager callbackManager;

    public static RegisterInitialFragment createInstance() {
        return new RegisterInitialFragment();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INITIAL_REGISTER;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_initial, parent, false);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        registerButton = (LoginTextView) view.findViewById(R.id.register);
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

        registerButton.setColor(Color.WHITE);
        registerButton.setBorderColor(R.color.black);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventRegisterChannel(AppEventTracking.GTMCacheValue.EMAIL);
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                        AppEventTracking.GTMCacheValue.EMAIL);
                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
                startActivityForResult(RegisterEmailActivity.getCallingIntent(getActivity()),
                        REQUEST_REGISTER_EMAIL);

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
                startActivityForResult(Login.getCallingIntent(getActivity()), REQUEST_LOGIN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN_WEBVIEW) {
            handleRegisterWebview(resultCode, data);
        } else if (requestCode == REQUEST_REGISTER_EMAIL && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        } else if (requestCode == REQUEST_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            getActivity().finish();
        } else if (requestCode == RC_SIGN_IN_GOOGLE){
            if (data != null) {
                GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
                String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);

                LoginGoogleModel model = new LoginGoogleModel();
                model.setFullName(googleSignInAccount.getDisplayName());
                model.setGoogleId(googleSignInAccount.getId());
                model.setEmail(googleSignInAccount.getEmail());
                model.setAccessToken(accessToken);

                UnifyTracking.eventMoRegistrationStart(
                        AppEventTracking.GTMCacheValue.GMAIL);

                presenter.registerGoogle(model);
            }
        }
    }

    private void handleRegisterWebview(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
        } else {
            Bundle bundle = data.getBundleExtra("bundle");
            if (bundle.getString("path").contains("error")) {
                NetworkErrorHelper.showSnackbar(getActivity(), bundle.getString(ARGS_MESSAGE));
            } else if (bundle.getString("path").contains("code")) {
                presenter.registerWebview(getActivity(), bundle);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void showLoadingDiscover() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount() - 1;
        if (!(linearLayout.getChildAt(lastPos) instanceof ProgressBar))
            linearLayout.addView(pb, linearLayout.getChildCount());
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
            String color = listProvider.get(i).getColor();
            int colorInt;
            if (color == null) {
                colorInt = Color.parseColor(COLOR_WHITE);
            } else {
                colorInt = Color.parseColor(color);
            }
            LoginTextView loginTextView = new LoginTextView(getActivity(), colorInt);
            loginTextView.setTextRegister(listProvider.get(i).getName());
            loginTextView.setImage(listProvider.get(i).getImage());
            loginTextView.setRoundCorner(10);

            setDiscoverOnClickListener(listProvider.get(i), loginTextView);

            if (linearLayout != null) {
                linearLayout.addView(loginTextView, linearLayout.getChildCount(), layoutParams);
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
                        onRegisterGooglelick(discoverItemViewModel);
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
        UnifyTracking.eventRegisterChannel(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                AppEventTracking.GTMCacheValue.FACEBOOK);
        UnifyTracking.eventMoRegistrationStart(
                com.tokopedia.core.analytics.AppEventTracking.GTMCacheValue.FACEBOOK);
        presenter.getFacebookCredential(this, callbackManager);


    }

    private void onRegisterGooglelick(DiscoverItemViewModel discoverItemViewModel) {
        UnifyTracking.eventRegisterChannel(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                AppEventTracking.GTMCacheValue.GMAIL);

        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);

    }

    private void onRegisterWebviewClick(DiscoverItemViewModel discoverItemViewModel) {
        WebViewLoginFragment newFragment = WebViewLoginFragment
                .createInstance(discoverItemViewModel.getUrl());
        newFragment.setTargetFragment(RegisterInitialFragment.this, REQUEST_LOGIN_WEBVIEW);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        newFragment.show(fragmentTransaction, WebViewLoginFragment.class.getSimpleName());
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(discoverItemViewModel
                .getName());

    }


    @Override
    public void dismissLoadingDiscover() {
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout.getChildAt(lastPos) instanceof ProgressBar)
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        if (container != null)
            container.setVisibility(View.GONE);
        if (loginButton != null)
            loginButton.setVisibility(View.GONE);
    }

    @Override
    public void onErrorGetFacebookCredential(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessGetFacebookCredential(AccessToken accessToken) {
        presenter.registerFacebook(accessToken);
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (container != null)
            container.setVisibility(View.VISIBLE);
        if (loginButton != null)
            loginButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorRegisterSosmed(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onGoToLogin() {

    }

    @Override
    public void onGoToCreatePasswordPage(GetUserInfoDomainData userInfoDomainData) {
        startActivityForResult(CreatePasswordActivity.getCallingIntent(getActivity(),
                new CreatePasswordModel(
                        userInfoDomainData.getEmail(),
                        userInfoDomainData.getFullName(),
                        userInfoDomainData.getBdayYear(),
                        userInfoDomainData.getBdayMonth(),
                        userInfoDomainData.getBdayDay(),
                        userInfoDomainData.getCreatePasswordList())),
                REQUEST_CREATE_PASSWORD);
    }

    @Override
    public void clearToken() {
        presenter.clearToken();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
