package com.tokopedia.inbox.inboxmessageold.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.inboxmessageold.listener.SendMessageFragmentView;
import com.tokopedia.inbox.inboxmessageold.presenter.SendMessagePresenter;
import com.tokopedia.inbox.inboxmessageold.presenter.SendMessagePresenterImpl;

import butterknife.BindView;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessageFragment extends BasePresenterFragment<SendMessagePresenter>
        implements SendMessageFragmentView {

    public static final java.lang.String PARAM_OWNER_FULLNAME = "owner_fullname";
    public static final java.lang.String PARAM_CUSTOM_SUBJECT = "custom_subject";
    public static final java.lang.String PARAM_CUSTOM_MESSAGE = "custom_message";
    public static final java.lang.String PARAM_SHOP_ID = "to_shop_id";
    public static final java.lang.String PARAM_USER_ID = "to_user_id";
    public static final String PARAM_SOURCE = "source";


    @BindView(R2.id.send_msg_to)
    EditText sendTo;

    @BindView(R2.id.send_msg_content)
    EditText sendContent;

    @BindView(R2.id.send_msg_subject)
    EditText sendSubject;

    @BindView(R2.id.send_msg_content_layout)
    TextInputLayout sendContentLayout;

    @BindView(R2.id.send_msg_subject_layout)
    TextInputLayout sendSubjectLayout;

    TkpdProgressDialog mProgressDialog;


    public static SendMessageFragment createInstance(Bundle extras) {
        SendMessageFragment fragment = new SendMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(com.tokopedia.core.R.menu.talk_add_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == com.tokopedia.core.R.id.action_send) {
            presenter.doSendMessage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
    }

    @Override
    protected void initialPresenter() {
        presenter = new SendMessagePresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return com.tokopedia.core.R.layout.fragment_send_message;
    }

    @Override
    protected void initView(View view) {
        sendTo.setText(getArguments().getString(PARAM_OWNER_FULLNAME));
        sendSubject.setText(getArguments().getString(PARAM_CUSTOM_SUBJECT, ""));
        sendContent.setText(getArguments().getString(PARAM_CUSTOM_MESSAGE, ""));
    }

    @Override
    protected void setViewListener() {
        sendSubject.addTextChangedListener(watcher(sendSubjectLayout));
        sendContent.addTextChangedListener(watcher(sendContentLayout));

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getSubject() {
        return sendSubject.getText().toString();
    }

    @Override
    public String getContent() {
        return sendContent.getText().toString();
    }

    @Override
    public void setSubjectError(String error) {
        setWatcherError(sendSubjectLayout, error);
    }

    @Override
    public void setContentError(String error) {
        setWatcherError(sendContentLayout, error);
    }

    @Override
    public void removeError() {
        setWatcherError(sendContentLayout, null);
        setWatcherError(sendSubjectLayout, null);

    }

    private void setWatcherError(TextInputLayout watcher, String errorMessage) {
        if (errorMessage == null)
            watcher.setErrorEnabled(false);
        else
            watcher.setErrorEnabled(true);
        watcher.setError(errorMessage);
        watcher.requestFocus();
    }

    @Override
    public void showError(String error, View.OnClickListener listener) {
        finishLoading();
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(com.tokopedia.core.R.string.title_retry), listener).show();
    }

    @Override
    public void finishLoading() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    public void showLoading() {

        if (mProgressDialog == null && getActivity() != null)
            mProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (mProgressDialog != null && getActivity() != null)
            mProgressDialog.showDialog();
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        sendSubject.setEnabled(isEnabled);
        sendContent.setEnabled(isEnabled);
        setHasOptionsMenu(isEnabled);
    }

    @Override
    public void showError(String error) {
        finishLoading();
        if (error.equals("")) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), error);
        }

    }

    @Override
    public void onDestroyView() {
        finishLoading();
        presenter.onDestroyView();
        super.onDestroyView();

    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWatcherError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
