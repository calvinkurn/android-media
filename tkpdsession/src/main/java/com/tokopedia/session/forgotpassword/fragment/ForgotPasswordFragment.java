package com.tokopedia.session.forgotpassword.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
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

public class ForgotPasswordFragment extends BasePresenterFragment<ForgotPasswordFragmentPresenter>
        implements ForgotPasswordFragmentView {

    private static final String ARGS_EMAIL = "ARGS_EMAIL";
    private static final String ARGS_AUTO_RESET = "ARGS_AUTO_RESET";
    private static final String ARGS_REMOVE_FOOTER = "ARGS_REMOVE_FOOTER";
    @BindView(R2.id.front_view)
    View FrontView;
    @BindView(R2.id.success_view)
    View SuccessView;
    @BindView(R2.id.email_send)
    TextView EmailSend;
    @BindView(R2.id.send_button)
    TextView SendButton;
    @BindView(R2.id.email)
    EditText Email;
    @BindView(R2.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R2.id.register_button)
    TextView registerButton;

    TkpdProgressDialog progressDialog;

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
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getArguments().getBoolean(ARGS_AUTO_RESET)) {
            onSuccessResetPassword();
        }
        if (getArguments().getBoolean(ARGS_REMOVE_FOOTER, false)) {
            registerButton.setVisibility(View.GONE);
        }
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

        presenter = new ForgotPasswordFragmentPresenterImpl(this,
                new ForgotPasswordRetrofitInteractorImpl(new AccountsService(bundle)),
                new CompositeSubscription());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_forgotpassword;
    }

    @Override
    protected void initView(View view) {
        if (!getArguments().getString(ARGS_EMAIL, "").equals("")) {
            Email.setText(getArguments().getString(ARGS_EMAIL));
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

    @Override
    protected void setViewListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        Email.addTextChangedListener(watcher(tilEmail));
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), Email);
                presenter.resetPassword();
            }
        });

    }

    private void goToRegister() {
        ((TkpdCoreRouter) getActivity().getApplication()).goToRegister(context);
        getActivity().finish();
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

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
        Email.setError(null);

    }

    @Override
    public EditText getEmail() {
        return Email;
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
        FrontView.setVisibility(View.GONE);
        SuccessView.setVisibility(View.VISIBLE);
        String myData = getString(R.string.title_reset_success_hint_1) + "\n"
                + Email.getText().toString() + ".\n"
                + getString(R.string.title_reset_success_hint_2);

        EmailSend.setText(myData);

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
}
