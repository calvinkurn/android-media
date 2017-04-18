package com.tokopedia.session.activation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.view.presenter.ChangeEmailPresenter;
import com.tokopedia.session.activation.view.presenter.ChangeEmailPresenterImpl;
import com.tokopedia.session.activation.view.viewListener.ChangeEmailView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailFragment extends BasePresenterFragment<ChangeEmailPresenter>
        implements ChangeEmailView {

    public static final int ACTION_CHANGE_EMAIL = 111;
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    EditText oldEmailEditText;
    EditText newEmailEditText;
    EditText passwordEditText;
    TextView changeEmailButton;

    TkpdProgressDialog progressDialog;

    public static ChangeEmailFragment createInstance() {
        return new ChangeEmailFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getActivity().getIntent().getExtras() != null)
            oldEmailEditText.setText(getActivity().getIntent().getExtras().getString(EXTRA_EMAIL, ""));
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
        presenter = new ChangeEmailPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_change_email;
    }

    @Override
    protected void initView(View view) {
        oldEmailEditText = (EditText) view.findViewById(R.id.old_email);
        newEmailEditText = (EditText) view.findViewById(R.id.new_email);
        passwordEditText = (EditText) view.findViewById(R.id.password);
        changeEmailButton = (TextView) view.findViewById(R.id.change_email_button);

    }

    @Override
    protected void setViewListener() {
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), getView());
                presenter.getPass().setOldEmail(oldEmailEditText.getText().toString());
                presenter.getPass().setNewEmail(newEmailEditText.getText().toString());
                presenter.getPass().setPassword(passwordEditText.getText().toString());
                presenter.changeEmail();
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
    public EditText getOldEmailEditText() {
        return oldEmailEditText;
    }

    @Override
    public EditText getNewEmailEditText() {
        return newEmailEditText;
    }

    @Override
    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    @Override
    public void onErrorChangeEmail(String errorMessage) {
        finishLoadingProgress();
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessChangeEmail() {
        finishLoadingProgress();
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_EMAIL,
                newEmailEditText.getText().toString());
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void onDestroyView() {
        progressDialog = null;
        presenter.unsubscribeObservable();
        super.onDestroyView();
    }
}