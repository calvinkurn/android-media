package com.tokopedia.otp.phoneverification.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumber;
import com.tokopedia.otp.phoneverification.view.presenter.ChangePhoneNumberPresenter;
import com.tokopedia.session.R;
import com.tokopedia.util.CustomPhoneNumberUtil;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nisie on 2/24/17.
 */

public class ChangePhoneNumberFragment extends BaseDaggerFragment
        implements ChangePhoneNumber.View {

    public static final int ACTION_CHANGE_PHONE_NUMBER = 111;
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    public static ChangePhoneNumberFragment createInstance() {
        return new ChangePhoneNumberFragment();
    }

    EditText phoneNumberEditText;
    TextView changePhoneNumberButton;
    TkpdProgressDialog progressDialog;

    @Inject
    ChangePhoneNumberPresenter presenter;

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_CHANGE_PHONE_NUMBER;
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
        View view = inflater.inflate(R.layout.fragment_change_phone_number, container, false);
        phoneNumberEditText = (EditText) view.findViewById(R.id.phone_number);
        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);
        phoneNumberEditText.addTextChangedListener(watcher(phoneNumberEditText));
        setViewListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity().getIntent().getExtras() != null) {
            phoneNumberEditText.setText(getActivity().getIntent().getExtras().getString(EXTRA_PHONE_NUMBER, ""));
        }
    }

    private TextWatcher watcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = CustomPhoneNumberUtil.transform(s.toString());
                if (s.toString().length() != phone.length()) {
                    editText.removeTextChangedListener(this);
                    editText.setText(phone);
                    editText.setSelection(phone.length());
                    editText.addTextChangedListener(this);
                }
            }
        };
    }

    private void setViewListener() {
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberEditText != null && !phoneNumberEditText.getText().toString().isEmpty()) {
                    KeyboardHandler.DropKeyboard(getActivity(), phoneNumberEditText);
                    showLoading();
                    presenter.changePhoneNumber(phoneNumberEditText.getText().toString().replace("-", ""));
                } else {
                    NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.please_fill_phone_number));
                }
            }
        });
    }

    private void showLoading() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        }
        if (progressDialog != null) {
            progressDialog.showDialog();
        }
    }


    @Override
    public void onSuccessChangePhoneNumber() {
        finishLoading();
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_PHONE_NUMBER,
                phoneNumberEditText.getText().toString());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private void finishLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public EditText getPhoneNumberEditText() {
        return phoneNumberEditText;
    }

    @Override
    public void onErrorChangePhoneNumber(String errorMessage) {
        finishLoading();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
