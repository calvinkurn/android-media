package com.tokopedia.session.forgotpassword.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.share.widget.SendButton;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core2.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.forgotpassword.interactor.ForgotPasswordRetrofitInteractorImpl;
import com.tokopedia.session.forgotpassword.listener.ForgotPasswordFragmentView;
import com.tokopedia.session.forgotpassword.presenter.ForgotPasswordFragmentPresenter;
import com.tokopedia.session.forgotpassword.presenter.ForgotPasswordFragmentPresenterImpl;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordFragment extends BaseDaggerFragment
        implements ForgotPasswordFragmentView {

    private static final String ARGS_EMAIL = "ARGS_EMAIL";
    private static final String ARGS_AUTO_RESET = "ARGS_AUTO_RESET";
    private static final String ARGS_REMOVE_FOOTER = "ARGS_REMOVE_FOOTER";
    View frontView;
    View successView;
    TextView emailSend;
    TextView sendButton;
    EditText emailEditText;
    TextInputLayout tilEmail;
    TextView registerButton;

    TkpdProgressDialog progressDialog;
    ForgotPasswordFragmentPresenter presenter;

    public static ForgotPasswordFragment createInstance(String email, boolean isAutoReset, boolean isRemoveFooter) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_EMAIL, email);
        bundle.putBoolean(ARGS_AUTO_RESET, isAutoReset);
        bundle.putBoolean(ARGS_REMOVE_FOOTER, isRemoveFooter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgotpassword, container, false);
        frontView = view.findViewById(R.id.front_view);
        successView = view.findViewById(R.id.success_view);
        emailSend = view.findViewById(R.id.email_send);
        sendButton = view.findViewById(R.id.send_button);
        emailEditText = view.findViewById(R.id.email);
        tilEmail = view.findViewById(R.id.til_email);
        registerButton = view.findViewById(R.id.register_button);

        initialPresenter();
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getBoolean(ARGS_AUTO_RESET)) {
            onSuccessResetPassword();
        }
        if (getArguments().getBoolean(ARGS_REMOVE_FOOTER, false)) {
            registerButton.setVisibility(View.GONE);
        }
    }

    private void initialPresenter() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        presenter = new ForgotPasswordFragmentPresenterImpl(this,
                new ForgotPasswordRetrofitInteractorImpl(new AccountsService(bundle)),
                new CompositeSubscription());
    }

    protected void initView(View view) {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        emailEditText.addTextChangedListener(watcher(tilEmail));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), emailEditText);
                presenter.resetPassword();
            }
        });

        if (!getArguments().getString(ARGS_EMAIL, "").equals("")) {
            emailEditText.setText(getArguments().getString(ARGS_EMAIL));
        }

        if (SessionHandler.isV4Login(getActivity())) {
            registerButton.setVisibility(View.GONE);
        } else {
            registerButton.setVisibility(View.VISIBLE);

            String sourceString = "Belum punya akun? " + "Daftar Sekarang";

            Spannable spannable = new SpannableString(sourceString);

            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View view) {

                                  }

                                  @Override
                                  public void updateDrawState(TextPaint ds) {
                                      ds.setUnderlineText(true);
                                      ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                                  }
                              }
                    , sourceString.indexOf("Daftar")
                    , sourceString.length()
                    , 0);

            registerButton.setText(spannable, TextView.BufferType.SPANNABLE);
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

    private void goToRegister() {
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getRegisterIntent
                (getActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void refresh() {
        //TODO
    }

    @Override
    public void resetError() {
        emailEditText.setError(null);

    }

    @Override
    public EditText getEmail() {
        return emailEditText;
    }

    @Override
    public void setEmailError(String errorMessage) {
        tilEmail.setErrorEnabled(true);
        tilEmail.setError(errorMessage);
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }

        if (progressDialog != null) {
            progressDialog.showDialog();
        }
    }

    @Override
    public void onErrorResetPassword(String errorMessage) {
        finishLoadingProgress();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessResetPassword() {
        finishLoadingProgress();
        frontView.setVisibility(View.GONE);
        successView.setVisibility(View.VISIBLE);
        String myData = getString(R.string.title_reset_success_hint_1) + "\n"
                + emailEditText.getText().toString() + ".\n"
                + getString(R.string.title_reset_success_hint_2);

        emailSend.setText(myData);

    }

    private void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    protected String getScreenName() {
        return LoginAnalytics.Screen.FORGOT_PASSWORD;
    }
}
