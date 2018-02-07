package com.tokopedia.otp.phoneverification.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.view.listener.ChangePhoneNumberView;
import com.tokopedia.otp.phoneverification.view.presenter.ChangePhoneNumberPresenter;
import com.tokopedia.otp.phoneverification.view.presenter.ChangePhoneNumberPresenterImpl;
import com.tokopedia.session.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nisie on 2/24/17.
 */

public class ChangePhoneNumberFragment extends BasePresenterFragment<ChangePhoneNumberPresenter>
        implements ChangePhoneNumberView {

    public static final int ACTION_CHANGE_PHONE_NUMBER = 111;
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    EditText phoneNumberEditText;
    TextView changePhoneNumberButton;
    TkpdProgressDialog progressDialog;
    SessionHandler sessionHandler;

    public static ChangePhoneNumberFragment createInstance() {
        return new ChangePhoneNumberFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getActivity().getIntent().getExtras() != null)
            phoneNumberEditText.setText(getActivity().getIntent().getExtras().getString(EXTRA_PHONE_NUMBER, ""));
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
        presenter = new ChangePhoneNumberPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_change_phone_number;
    }

    @Override
    protected void initView(View view) {
        phoneNumberEditText = (EditText) view.findViewById(R.id.phone_number);
        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);

        phoneNumberEditText.addTextChangedListener(watcher(phoneNumberEditText));

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

    @Override
    protected void setViewListener() {
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumberEditText!=null && !phoneNumberEditText.getText().toString().isEmpty()) {
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
        if(progressDialog == null && getActivity()!= null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if(progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    protected void initialVar() {
        sessionHandler = new SessionHandler(getActivity());
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSuccessChangePhoneNumber() {
        finishLoading();
        sessionHandler.setPhoneNumber(phoneNumberEditText.getText().toString());
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_PHONE_NUMBER,
                phoneNumberEditText.getText().toString());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private void finishLoading() {
        if(progressDialog!= null)
        progressDialog.dismiss();
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
        presenter.unsubscribeObservable();
    }
}
