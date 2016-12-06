package com.tokopedia.session.session.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.session.session.presenter.ForgotPassword;
import com.tokopedia.session.session.presenter.ForgotPasswordImpl;
import com.tokopedia.session.session.presenter.ForgotPasswordView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.var.TkpdState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author m.normansyah
 * @since 18/11/2015
 * @version 2
 */
public class ForgotPasswordFragment extends TkpdBaseV4Fragment implements ForgotPasswordView{

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

    ForgotPassword forgotPassword;
    TkpdProgressDialog progressDialog;
    boolean isProgressDialog;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgotPassword = new ForgotPasswordImpl(this);
        forgotPassword.fetchDataAfterRotate(savedInstanceState);
        forgotPassword.initDataInstances(getActivity());
        setProgressDialog();
    }

    private void setProgressDialog() {
        if(isProgressDialog)
        {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.showDialog();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_forgot_password, container, false);
        unbinder = ButterKnife.bind(this,rootView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SessionView) getActivity()).moveToRegisterInitial();
            }
        });
        String sourceString = "Belum punya akun? "+ "Daftar Sekarang";

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
                ,0);

        registerButton.setText(spannable, TextView.BufferType.SPANNABLE);

        SendButton.setBackgroundResource(R.drawable.bg_rounded_corners);
        return rootView;
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
                if (s.length() == 0){
                    setWrapperError(wrapper, getString(R.string.error_field_required));
                }
            }
        };
    }


    private void setWrapperError(TextInputLayout wrapper, String s) {
        if(s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        }else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_FORGOT_PASSWORD;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLocalyticFlow();
        ScreenTracking.screen(getScreenName());
        forgotPassword.subscribe();
        forgotPassword.initData(getActivity());
        Email.addTextChangedListener(watcher(tilEmail));
    }

    @Override
    public void onPause() {
        super.onPause();
        forgotPassword.unSubscribe();
    }

    @OnClick(R2.id.send_button)
    public void onSendButtonClick(){
        Email.setError(null);
        if(Email.length() > 0)
            //SendRequest();
            if(CommonUtils.EmailValidation(Email.getText().toString())) {
                KeyboardHandler.DropKeyboard(getActivity(),Email);
                forgotPassword.resetPassword(Email.getText().toString());
                showProgressDialog();
                tilEmail.setErrorEnabled(false);
                tilEmail.setError(null);
                UnifyTracking.eventForgotPassword();
            }else {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError(getString(R.string.error_invalid_email));
            }
        else {
            tilEmail.setErrorEnabled(true);
            tilEmail.setError(getString(R.string.error_field_required));
        }
    }

    public void showProgressDialog() {
        if(progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.setCancelable(false);
        progressDialog.showDialog();
        isProgressDialog = true;
    }

    public void dismissProgressDialog(){
        if (isProgressDialog && progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean checkIsLoading() {
        if(progressDialog != null && progressDialog.isProgress()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setIsProgressDialog(boolean aBoolean) {
        isProgressDialog = aBoolean;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyboardHandler.DropKeyboard(getActivity(), Email);
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        forgotPassword.saveDataBeforeRotate(outState);
    }

    @Override
    public void setLocalyticFlow() {
        CommonUtils.dumper("LocalTag : Forgot Password");
        String screenName = getString(R.string.forgot_password_page);
        ScreenTracking.screenLoca(screenName);
    }

    @Override
    public void displaySuccessView(boolean isSuccess) {
        if(isSuccess)
            SuccessView.setVisibility(View.VISIBLE);
        else
            SuccessView.setVisibility(View.GONE);

        KeyboardHandler.DropKeyboard(getActivity(),Email);
    }

    @Override
    public void displayFrontView(boolean isFrontView) {
        if(isFrontView)
            FrontView.setVisibility(View.VISIBLE);
        else
            FrontView.setVisibility(View.GONE);
    }

    @Override
    public void setTextEmailSend(String email) {
//        EmailSend.setText(Email.getText().toString());
        String myData = getString(R.string.title_reset_success_hint_1) + "\n"
                            + email + ".\n"
                            + getString(R.string.title_reset_success_hint_2);

        EmailSend.setText(myData);
    }

    @Override
    public void moveToRegister(String email) {
        if(getActivity() instanceof SessionView){
            ((SessionView)getActivity()).moveToRegister();
        }
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.FORGOT_PASSWORD;
    }

    @Override
    public void ariseRetry(int type, Object... data) {
        throw new RuntimeException("don't call this method !!");
    }

    @Override
    public void setData(int type, Bundle data) {
        forgotPassword.setData(type,data);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        throw new RuntimeException("don't call this method !!");
    }

    @Override
    public void onMessageError(int type, Object... data) {
        String text = (String) data[0];
        forgotPassword.setError(type, text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        forgotPassword.unSubscribe();
    }
}
