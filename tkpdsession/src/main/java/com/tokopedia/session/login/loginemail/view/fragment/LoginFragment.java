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
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.tokopedia.otp.securityquestion.view.activity.SecurityQuestionActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.forgotpassword.activity.ForgotPasswordActivity;
import com.tokopedia.session.login.loginemail.domain.model.LoginEmailDomain;
import com.tokopedia.session.login.loginemail.view.presenter.LoginPresenter;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
import com.tokopedia.session.register.view.activity.RegisterInitialActivity;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;
import com.tokopedia.di.DaggerSessionComponent;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginFragment extends BaseDaggerFragment
        implements Login.View {

    private static final String COLOR_WHITE = "#FFFFFF";
    private static final String FACEBOOK = "facebook";
    private static final String GPLUS = "gplus";
    private static final String PHONE_NUMBER = "tokocash";

    private static final int REQUEST_PHONE_NUMBER = 101;
    private static final int REQUEST_SECURITY_QUESTION = 102;

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
    CheckBox rememberAccountCheck;

    ArrayAdapter<String> autoCompleteAdapter;

    @Inject
    LoginPresenter presenter;


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
        rememberAccountCheck = view.findViewById(R.id.remember_account);
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
                            presenter.login(emailEditText.getText().toString(),
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
                presenter.login(emailEditText.getText().toString(),
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

        rememberAccountCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if (!state) {
                    presenter.clearRememberMe();
                }
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
    }

    private void goToRegisterInitial() {
        startActivity(RegisterInitialActivity.getCallingIntent(getActivity()));
        getActivity().finish();
    }


    @Override
    public void resetError() {

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

    @Override
    public void goToSecurityQuestion(LoginEmailDomain loginDomain) {
        Intent intent = SecurityQuestionActivity.getCallingIntent(getActivity(),
                loginDomain.getLoginResult().getSecurityDomain(),
                loginDomain.getInfo().getGetUserInfoDomainData().getName(),
                loginDomain.getInfo().getGetUserInfoDomainData().getEmail(),
                loginDomain.getInfo().getGetUserInfoDomainData().getPhone());
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
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

    private void setDiscoverListener(final DiscoverItemViewModel discoverItemViewModel, LoginTextView tv) {
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
                    onLoginWebviewClick(discoverItemViewModel);
                }
            });
        }
    }

    private void onLoginWebviewClick(DiscoverItemViewModel discoverItemViewModel) {
        UnifyTracking.eventCTAAction(discoverItemViewModel.getName());
        presenter.loginWebview();
    }

    private void onLoginPhoneNumberClick() {
        UnifyTracking.eventCTAAction(PHONE_NUMBER);
        Intent intent = LoginPhoneNumberActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_PHONE_NUMBER);
    }

    private void onLoginGoogleClick() {
        UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
        presenter.loginGoogle();
//        LoginFragmentPermissionsDispatcher.onGooglePlusClickedWithCheck(com.tokopedia.session.session.fragment.LoginFragment.this);
    }

    private void onLoginFacebookClick() {
        UnifyTracking.eventCTAAction(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
        presenter.loginFacebook();
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
