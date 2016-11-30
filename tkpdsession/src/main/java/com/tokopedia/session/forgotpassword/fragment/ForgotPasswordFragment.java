package com.tokopedia.session.forgotpassword.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.session.forgotpassword.listener.ForgotPasswordFragmentView;
import com.tokopedia.session.forgotpassword.presenter.ForgotPasswordFragmentPresenter;
import com.tokopedia.session.forgotpassword.presenter.ForgotPasswordFragmentPresenterImpl;

import butterknife.BindView;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordFragment extends BasePresenterFragment<ForgotPasswordFragmentPresenter> implements ForgotPasswordFragmentView {

    @BindView(R2.id.front_view)
    View mainView;
    @BindView(R2.id.success_view)
    View successView;
    @BindView(R2.id.email_send)
    TextView emailSend;
    @BindView(R2.id.send_button)
    TextView sendButton;
    @BindView(R2.id.email)
    EditText email;
    @BindView(R2.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R2.id.register_button1)
    TextView registerButton1;
    @BindView(R2.id.register_button2)
    TextView registerButton2;

    public static ForgotPasswordFragment createInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle bundle = new Bundle();
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
        //TODO
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
        presenter = new ForgotPasswordFragmentPresenterImpl(this);
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
        String sourceString = "Belum punya akun?";
        registerButton1.setText(Html.fromHtml(sourceString));
        String sourceString2 = "&nbsp; <u><b>Daftar Sekarang</b></u>";
        registerButton2.setText(Html.fromHtml(sourceString2));
    }

    @Override
    protected void setViewListener() {
        registerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                /*((SessionView) getActivity()).moveToRegister();*/
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void finishLoading() {
        //TODO
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void setLoading() {

    }

    @Override
    public void showErrorMessage() {
        //TODO
    }

    @Override
    public void removeError() {

    }

    @Override
    public void setActionsEnabled(Boolean isEnabled) {

    }

    @Override
    public void resetPassword() {

    }

    @Override
    public void refresh() {
        //TODO
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
