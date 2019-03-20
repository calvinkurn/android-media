package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.view.activity.AddNameRegisterPhoneActivity;
import com.tokopedia.session.register.registerphonenumber.view.listener.AddNameListener;
import com.tokopedia.session.register.registerphonenumber.view.presenter.AddNamePresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;
import com.tokopedia.session.register.view.util.ViewUtil;
import com.tokopedia.track.TrackApp;

import javax.inject.Inject;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNameRegisterPhoneFragment extends BaseDaggerFragment implements AddNameListener.View {


    private String phoneNumber;

    protected EditText etName;
    private TextView bottomInfo, message;
    protected TextView btnContinue;
    private TkpdProgressDialog progressDialog;
    private TkpdHintTextInputLayout wrapperName;

    private boolean isError = false;

    @Inject
    protected AddNamePresenter presenter;

    public static AddNameRegisterPhoneFragment newInstance(Bundle bundle) {
        AddNameRegisterPhoneFragment fragment = new AddNameRegisterPhoneFragment();
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
        message = (TextView) view.findViewById(R.id.message);
        wrapperName = (TkpdHintTextInputLayout) view.findViewById(R.id.wrapper_name);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setView();
        setViewListener();
        disableNextButton();
    }

    private void setView() {
        phoneNumber = getArguments().getString(AddNameRegisterPhoneActivity.PARAM_PHONE);
        disableNextButton();
        btnContinue.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

        String joinString = getString(R.string.detail_term_and_privacy) +
                "<br>" + getString(R.string.link_term_condition) +
                " serta " + getString(R.string.link_privacy_policy);

        bottomInfo.setText(MethodChecker.fromHtml(joinString));
        bottomInfo.setMovementMethod(LinkMovementMethod.getInstance());
        ViewUtil.stripUnderlines(bottomInfo);
    }

    protected void setViewListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    enableNextButton();
                } else {
                    disableNextButton();
                }
                if (isError) {
                    hideValidationError();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onContinueClick();
            }
        });
        btnContinue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == R.id.btn_continue || id == EditorInfo.IME_NULL) {
                    onContinueClick();
                    return true;
                }
                return false;
            }
        });
    }

    protected void onContinueClick() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        presenter.registerPhoneNumberAndName(etName.getText().toString());
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
        showValidationError(error);
    }

    @Override
    public void onSuccessRegister(LoginRegisterPhoneNumberModel model) {
        dismissLoading();
        if(model.getMakeLoginDomain() != null) {
            TrackApp.getInstance().getAppsFlyer().sendAppsflyerRegisterEvent(
                    String.valueOf(model.getMakeLoginDomain().getUserId()),
                    LoginAnalytics.Label.PHONE_NUMBER);
        }
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
    public void showValidationError(String error) {
        isError = true;
        wrapperName.setErrorEnabled(true);
        wrapperName.setError(error);
        message.setVisibility(View.GONE);
    }

    @Override
    public void hideValidationError() {
        isError = false;
        wrapperName.setErrorEnabled(false);
        wrapperName.setError("");
        message.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessAddName() {
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
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
