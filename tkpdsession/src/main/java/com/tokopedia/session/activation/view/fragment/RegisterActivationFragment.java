package com.tokopedia.session.activation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.activation.view.presenter.RegisterActivationPresenter;
import com.tokopedia.session.activation.view.presenter.RegisterActivationPresenterImpl;
import com.tokopedia.session.activation.view.viewListener.RegisterActivationView;

/**
 * Created by nisie on 1/31/17.
 */

public class RegisterActivationFragment extends BasePresenterFragment<RegisterActivationPresenter>
        implements RegisterConstant, RegisterActivationView {

    private static final String ARGS_EMAIL = "ARGS_EMAIL";

    TextView activationText;
    EditText verifyCode;
    TextView activateButton;
    TextView footer;
    TkpdProgressDialog progressDialog;

    public static RegisterActivationFragment createInstance(String email) {
        RegisterActivationFragment fragment = new RegisterActivationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_EMAIL, email);
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
        presenter = new RegisterActivationPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register_activation;
    }

    @Override
    protected void initView(View view) {
        activationText = (TextView) view.findViewById(R.id.activation_text);
        verifyCode = (EditText) view.findViewById(R.id.input_verify_code);
        activateButton = (TextView) view.findViewById(R.id.verify_button);
        footer = (TextView) view.findViewById(R.id.footer);

        String activateText = getString(R.string.activation_header_text) + " <b>" + getArguments().getString(ARGS_EMAIL, "") + "</b>";
        activationText.setText(MethodChecker.fromHtml(activateText).toString());

        Spannable spannable = new SpannableString(getString(R.string.activation_resend_email));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(MethodChecker.getColor(getActivity(),
                                          com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , getString(R.string.activation_resend_email).indexOf("Kirim")
                , getString(
                        R.string.activation_resend_email).length()
                , 0);

        footer.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void setViewListener() {
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.activateAccount();
            }
        });
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.resendActivation();
            }
        });

        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    activateButton.setEnabled(true);
                    MethodChecker.setBackground(activateButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button));
                    activateButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));

                } else {
                    activateButton.setEnabled(false);
                    MethodChecker.setBackground(activateButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.cards_grey));
                    activateButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                }
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
        return getArguments().getString(ARGS_EMAIL, "");
    }

    @Override
    public void finishLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
