package com.tokopedia.session.addchangeemail.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailVerificationActivity;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailListener;
import com.tokopedia.session.addchangeemail.view.presenter.AddEmailPresenter;

import javax.inject.Inject;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailFragment extends BaseDaggerFragment implements AddEmailListener.View {

    private static final int REQUEST_VERIFY_EMAIL = 1234;

    private TkpdHintTextInputLayout wrapperEmail;
    private EditText etEmail;
    private TextView tvMessage, tvError;
    private Button btnContinue;
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
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        wrapperEmail = (TkpdHintTextInputLayout) view.findViewById(R.id.wrapper_email);
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
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerSessionComponent.inject(this);
    }

    private void initView() {
        disableNextButton();
    }

    private void initViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitEmail(etEmail.getText().toString());
            }
        });

        etEmail.addTextChangedListener(emailWatcher(wrapperEmail));
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
        Intent intent = AddEmailVerificationActivity.newInstance(getActivity(), etEmail.getText().toString());
        startActivityForResult(intent, REQUEST_VERIFY_EMAIL);
    }

    @Override
    public void onErrorCheckEmail(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
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

    private TextWatcher emailWatcher(final TkpdHintTextInputLayout wrapper) {
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
                } else if (!CommonUtils.EmailValidation(etEmail.getText().toString())) {
                    setWrapperError(wrapper, getString(R.string.wrong_email_format));
                }
            }
        };
    }

    private void enableButton(Button button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(Button button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(true);
    }

    private void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
            tvMessage.setVisibility(View.GONE);
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
}
