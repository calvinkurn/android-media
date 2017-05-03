package com.tokopedia.session.activation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.activation.interactor.ActivationNetworkInteractorImpl;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.activation.presenter.RegisterActivationPresenter;
import com.tokopedia.session.activation.presenter.RegisterActivationPresenterImpl;
import com.tokopedia.session.activation.viewListener.RegisterActivationView;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/31/17.
 */

public class RegisterActivationFragment extends BasePresenterFragment<RegisterActivationPresenter>
        implements RegisterConstant, RegisterActivationView {

    private static final String ARGS_EMAIL = "ARGS_EMAIL";
    private static final String ARGS_NAME = "ARGS_NAME";


    @BindView(R2.id.head)
    TextView nameEditText;
    @BindView(R2.id.email)
    TextView email;
    @BindView(R2.id.resend_button)
    TextView resendButton;

    TkpdProgressDialog progressDialog;

    public static RegisterActivationFragment createInstance(String email, String name) {
        RegisterActivationFragment fragment = new RegisterActivationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_EMAIL, email);
        bundle.putString(ARGS_NAME, name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
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
        return false;
    }

    @Override
    protected void initialPresenter() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);

        presenter = new RegisterActivationPresenterImpl(this,
                new ActivationNetworkInteractorImpl(new AccountsService(bundle)),
                new CompositeSubscription());
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.activity_activation_resent;
    }

    @Override
    protected void initView(View view) {
        resendButton.setBackgroundResource(com.tokopedia.core.R.drawable.bg_rounded_corners);
        nameEditText.setText("Halo,");
        email.setText(getArguments().getString(ARGS_EMAIL, ""));
//        SnackbarManager.make(getActivity(), "Akun anda belum diaktivasi. Cek email anda untuk mengaktivasi akun.", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void setViewListener() {
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHandler.DropKeyboard(getActivity(), email);
                resendButton.setText(getString(R.string.title_resend_activation_email));
                UnifyTracking.eventResendNotification();
                presenter.resendActivation();
            }
        });

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == com.tokopedia.core.R.id.email || id == EditorInfo.IME_NULL) {
                    presenter.resendActivation();
                    return true;
                }
                return false;
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
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void onErrorResendActivation(String errorMessage) {
        finishLoadingProgress();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessResendActivation(String statusMessage) {
        finishLoadingProgress();
        SnackbarManager.make(getActivity(), statusMessage,
                Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    public String getEmail() {
        return email.getText().toString();
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
