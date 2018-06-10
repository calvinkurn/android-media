package com.tokopedia.session.addchangeemail.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerUserComponent;
import com.tokopedia.di.UserComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangeemail.di.DaggerAddChangeEmailComponent;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailVerificationActivity;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.presenter.AddEmailPresenter;

import javax.inject.Inject;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailFragment extends BaseDaggerFragment implements AddEmailListener.View {

    private static final int REQUEST_VERIFY_EMAIL = 1234;

    private EditText etEmail;
    private TextView tvMessage, tvError;
    private TextView btnContinue;
    private TkpdHintTextInputLayout wrapperEmail;
    private TkpdProgressDialog progressDialog;

    @Inject
    AddEmailPresenter presenter;

    public static AddEmailFragment newInstance() {
        AddEmailFragment addEmailFragment = new AddEmailFragment();
        Bundle bundle = new Bundle();
        addEmailFragment.setArguments(bundle);
        return addEmailFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_email, container, false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        btnContinue = (TextView) view.findViewById(R.id.btn_continue);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        wrapperEmail = (TkpdHintTextInputLayout) view.findViewById(R.id.wrapper_email) ;
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewListener();
    }

    @Override
    protected void initInjector() {
        UserComponent userComponent = DaggerUserComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerAddChangeEmailComponent.builder()
                .userComponent(userComponent)
                .build().inject(this);
    }

    private void initView() {
        disableNextButton();
        setDefaultHelper();
        btnContinue.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    private void setDefaultHelper() {
        tvMessage.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    private void initViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitEmail(etEmail.getText().toString());
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setTextError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setTextError(getString(R.string.error_field_required));
                } else if (!CommonUtils.EmailValidation(etEmail.getText().toString())) {
                    setTextError(getString(R.string.wrong_email_format));
                }
            }
        });
    }

    @Override
    public void enableNextButton() {
        enableButton(btnContinue);
    }

    @Override
    public void disableNextButton() {
        disableButton(btnContinue);
    }

    @Override
    public void onSuccessCheckEmail() {
        //intent to verification email
        startActivityForResult(
                AddEmailVerificationActivity.newInstance(getActivity(), etEmail.getText().toString()),
                REQUEST_VERIFY_EMAIL);
    }

    @Override
    public void onErrorCheckEmail(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
        setTextError(error);
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        progressDialog.showDialog();
    }

    private void enableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_12));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(false);
    }

    private void setTextError(String s) {
        if (TextUtils.isEmpty(s)) {
            setDefaultHelper();
            enableNextButton();
            wrapperEmail.setErrorEnabled(true);
        } else {
            wrapperEmail.setErrorEnabled(false);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(s);
            tvMessage.setVisibility(View.GONE);
            disableNextButton();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VERIFY_EMAIL) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
