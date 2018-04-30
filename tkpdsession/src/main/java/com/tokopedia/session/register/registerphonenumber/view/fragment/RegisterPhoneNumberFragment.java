package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.analytics.RegisterAnalytics;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
import com.tokopedia.session.register.registerphonenumber.view.activity.WelcomePageActivity;
import com.tokopedia.session.register.registerphonenumber.view.listener.RegisterPhoneNumber;
import com.tokopedia.session.register.registerphonenumber.view.presenter.RegisterPhoneNumberPresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;
import com.tokopedia.session.register.view.util.ViewUtil;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberFragment extends BaseDaggerFragment
        implements RegisterPhoneNumber.View {

    private static final int REQUEST_VERIFY_PHONE = 101;
    private static final int REQUEST_WELCOME_PAGE = 102;

    private EditText phoneNumber;
    private TextView nextButton;
    private TextView message;
    private TextView bottomInfo;
    private TkpdHintTextInputLayout phoneNumberLayout;

    TkpdProgressDialog progressDialog;

    @Inject
    RegisterPhoneNumberPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new RegisterPhoneNumberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return RegisterAnalytics.Screen.SCREEN_REGISTER_WITH_PHONE_NUMBER;
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
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_phone_number, parent, false);
        phoneNumber = view.findViewById(R.id.phone_number);
        message = view.findViewById(R.id.message);
        nextButton = view.findViewById(R.id.next_btn);
        bottomInfo = view.findViewById(R.id.botton_info);
        phoneNumberLayout = view.findViewById(R.id.wrapper_name);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        phoneNumber.requestFocus();
        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    message.setVisibility(View.VISIBLE);
                    presenter.checkPhoneNumber(phoneNumber.getText().toString());
                    handled = true;
                    phoneNumberLayout.setErrorEnabled(false);
                }
                return handled;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumberLayout.setErrorEnabled(false);
                message.setVisibility(View.VISIBLE);
                presenter.checkPhoneNumber(phoneNumber.getText().toString());
            }
        });

        String joinString = getString(R.string.detail_term_and_privacy) +
                "<br>" + getString(R.string.link_term_condition) +
                " serta " + getString(R.string.link_privacy_policy);

        bottomInfo.setText(MethodChecker.fromHtml(joinString));
        bottomInfo.setMovementMethod(LinkMovementMethod.getInstance());
        nextButton.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        ViewUtil.stripUnderlines(bottomInfo);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showErrorPhoneNumber(int resId) {
        showErrorPhoneNumber(getString(resId));
    }

    @Override
    public void goToVerifyAccountPage(String phoneNumber) {

        Intent intent = VerificationActivity.getCallingIntent(
                getActivity(),
                phoneNumber,
                RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER,
                true,
                RequestOtpUseCase.MODE_SMS
        );
        startActivityForResult(intent, REQUEST_VERIFY_PHONE);
    }

    @Override
    public void goToLoginPhoneNumber() {
        startActivity(LoginPhoneNumberActivity.getCallingIntentFromRegister(getActivity(), phoneNumber.getText().toString()));
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
    public void showErrorPhoneNumber(String errorMessage) {
        dismissLoading();
        message.setVisibility(View.GONE);
        phoneNumberLayout.setErrorEnabled(true);
        phoneNumberLayout.setError(errorMessage);
    }

    @Override
    public void showAlreadyRegisteredDialog(String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.phone_number_already_registered));
        builder.setMessage(String.format(getResources().getString(R.string.phone_number_already_registered_info), phoneNumber));
        builder.setPositiveButton(getResources().getString(R.string.phone_number_already_registered_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                goToLoginPhoneNumber();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.phone_number_already_registered_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void showConfirmationPhoneNumber(final String phoneNumber) {
        final String realPhoneNumberString = this.phoneNumber.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(phoneNumber));
        builder.setMessage(getResources().getString(R.string.phone_number_not_registered_info));
        builder.setPositiveButton(getResources().getString(R.string.phone_number_not_registered_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                goToVerifyAccountPage(realPhoneNumberString);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.phone_number_not_registered_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    public void doRegisterPhoneNumber() {
        presenter.registerPhoneNumber(phoneNumber.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VERIFY_PHONE
                && resultCode == Activity.RESULT_OK) {
            doRegisterPhoneNumber();
        } else if (requestCode == REQUEST_WELCOME_PAGE) {
            if (resultCode == Activity.RESULT_OK) {
                goToProfileCompletionPage();
            } else {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showSuccessRegisterPhoneNumber(LoginRegisterPhoneNumberModel model) {
        dismissLoading();
        UnifyTracking.eventTracking(LoginAnalytics.getEventSuccessRegisterPhoneNumber());
        if (model.getMakeLoginDomain().isLogin()) {
            goToWelcomePage();
        } else {
            goToLoginPhoneNumber();
        }
    }

    @Override
    public void showErrorRegisterPhoneNumber(String message) {
        dismissLoading();
        showSnackbar(message);
    }

    @Override
    public void dismissFocus() {
        phoneNumber.clearFocus();
    }

    private void showSnackbarErrorWithAction(String message) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                doRegisterPhoneNumber();
            }
        }).showRetrySnackbar();
    }

    private void showSnackbar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void goToWelcomePage() {
        startActivityForResult(WelcomePageActivity.newInstance(getActivity()), REQUEST_WELCOME_PAGE);
    }

    private void goToProfileCompletionPage() {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        Intent parentIntent = ((TkpdCoreRouter) getActivity().getApplicationContext()).getHomeIntent(getActivity());
        parentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent childIntent = new Intent(getActivity(), ProfileCompletionActivity.class);
        stackBuilder.addNextIntent(parentIntent);
        stackBuilder.addNextIntent(childIntent);
        getActivity().startActivities(stackBuilder.getIntents());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void enableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(false);
    }
}
