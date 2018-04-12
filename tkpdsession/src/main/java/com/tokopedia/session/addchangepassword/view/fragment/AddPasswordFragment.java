package com.tokopedia.session.addchangepassword.view.fragment;

import android.app.Activity;
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangepassword.view.listener.AddPasswordListener;
import com.tokopedia.session.addchangepassword.view.presenter.AddPasswordPresenter;

import javax.inject.Inject;

/**
 * @author by yfsx on 23/03/18.
 */

public class AddPasswordFragment extends BaseDaggerFragment implements AddPasswordListener.View {


    public static AddPasswordFragment newInstance(Bundle bundle) {
        AddPasswordFragment fragment = new AddPasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    AddPasswordPresenter presenter;

    private EditText etPassword;
    private TextView message, error;
    private Button btnContinue;

    private TkpdProgressDialog progressDialog;


    @Override
    protected String getScreenName() {
        return null;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_password, container, false);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        error = (TextView) view.findViewById(R.id.tv_error);
        message = (TextView) view.findViewById(R.id.tv_message);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setView();
        setViewListener();
    }

    private void setView() {
        disableButton(btnContinue);
    }

    private void setViewListener() {

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.checkPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitPassword(etPassword.getText().toString());
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
    public void onErrorSubmitPassword(String error) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.submitPassword(etPassword.getText().toString());
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onSuccessSubmitPassword() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
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

    private void enableButton(Button button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(Button button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
