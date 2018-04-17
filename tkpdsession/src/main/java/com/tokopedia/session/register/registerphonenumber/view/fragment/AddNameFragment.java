package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.view.activity.AddNameActivity;
import com.tokopedia.session.register.registerphonenumber.view.listener.AddNameListener;
import com.tokopedia.session.register.registerphonenumber.view.presenter.AddNamePresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;
import com.tokopedia.session.register.view.util.ViewUtil;

import javax.inject.Inject;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNameFragment extends BaseDaggerFragment implements AddNameListener.View {

    private static final int COUNT_CHAR_MIN = 3;
    private static final int COUNT_CHAR_MAX = 128;

    private String phoneNumber;

    private EditText etName;
    private TextView btnContinue, bottomInfo;
    private TkpdProgressDialog progressDialog;

    @Inject
    AddNamePresenter presenter;

    public static AddNameFragment newInstance(Bundle bundle) {
        AddNameFragment fragment = new AddNameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_name, container, false);
        etName = (EditText) view.findViewById(R.id.et_name);
        btnContinue = (TextView) view.findViewById(R.id.btn_continue);
        bottomInfo = (TextView) view.findViewById(R.id.bottom_info);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString(AddNameActivity.PARAM_PHONE, phoneNumber);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        phoneNumber = savedInstanceState.getString(AddNameActivity.PARAM_PHONE)
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setView();
        setViewListener();
    }

    private void setView() {
        phoneNumber = getArguments().getString(AddNameActivity.PARAM_PHONE);
        disableNextButton();
        btnContinue.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

        String joinString = getString(R.string.detail_term_and_privacy) +
                "<br>" + getString(R.string.link_term_condition) +
                " serta " + getString(R.string.link_privacy_policy);

        bottomInfo.setText(MethodChecker.fromHtml(joinString));
        bottomInfo.setMovementMethod(LinkMovementMethod.getInstance());
        ViewUtil.stripUnderlines(bottomInfo);
    }

    private void setViewListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidName(charSequence.toString())) {
                    enableNextButton();
                } else {
                    disableNextButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.registerPhoneNumberAndName(etName.getText().toString());
            }
        });
    }

    private boolean isValidName(String name) {
        if (name.length() < COUNT_CHAR_MIN) {
            return false;
        }
        else if (name.length() > COUNT_CHAR_MAX) {
            return false;
        }
        return true;
    }

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

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
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
    public void onErrorRegister(String error) {
        dismissLoading();
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void onSuccessRegister(LoginRegisterPhoneNumberModel model) {
        dismissLoading();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
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
}
