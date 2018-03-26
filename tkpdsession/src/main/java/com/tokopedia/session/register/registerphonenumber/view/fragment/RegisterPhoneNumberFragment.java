package com.tokopedia.session.register.registerphonenumber.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.registerphonenumber.view.activity.VerificationActivity;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.MethodItem;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.LoginPhoneNumberActivity;
import com.tokopedia.session.register.registerphonenumber.view.activity.WelcomePageActivity;
import com.tokopedia.session.register.registerphonenumber.view.listener.RegisterPhoneNumber;
import com.tokopedia.session.register.registerphonenumber.view.presenter.RegisterPhoneNumberPresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberFragment extends BaseDaggerFragment
        implements RegisterPhoneNumber.View {

    private static final int REQUEST_VERIFY_PHONE = 101;
    private static final int REQUEST_WELCOME_PAGE = 102;

    EditText phoneNumber;
    TextView nextButton;
    TextView message;
    TextView errorText;
    TextView bottomInfo;

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
        errorText = view.findViewById(R.id.error);
        bottomInfo = view.findViewById(R.id.botton_info);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        errorText.setVisibility(View.VISIBLE);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidNumber(charSequence.toString())) {
                    enableButton(nextButton);
                } else {
                    disableButton(nextButton);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (isValidNumber(v.getText().toString())) {
//                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                        presenter.checkPhoneNumber(phoneNumber.getText().toString());
                    }
                    handled = true;
                }
                return handled;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UnifyTracking.eventTracking(LoginPhoneNumberAnalytics.getLoginWithPhoneTracking());
                presenter.checkPhoneNumber(phoneNumber.getText().toString());
            }
        });
        nextButton.setClickable(false);

        String joinString = getString(com.tokopedia.core.R.string.detail_term_and_privacy) +
                "<br>" + getString(com.tokopedia.core.R.string.link_term_condition) +
                " serta " + getString(com.tokopedia.core.R.string.link_privacy_policy);

        bottomInfo.setText(MethodChecker.fromHtml(joinString));
        bottomInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean isValidNumber(String phoneNumber) {
        if (phoneNumber.length() == 0) {
            message.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getResources().getString(R.string.error_input_phone_number));
            return false;
        }
        if (phoneNumber.length() < 8) {
            message.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getResources().getString(R.string.error_char_count_under));
            return false;
        }

        if (phoneNumber.length() > 15) {
            message.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(getResources().getString(R.string.error_char_count_over));
            return false;
        }
        message.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.GONE);
        errorText.setText("");
        return true;
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
        startActivityForResult(VerificationActivity.getRegisterPhoneNumberVerificationIntent(
                getActivity(),
                phoneNumber,
                getListVerificationMethod()),
                REQUEST_VERIFY_PHONE);
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

    private ArrayList<MethodItem> getListVerificationMethod() {
        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                VerificationActivity.TYPE_SMS,
                R.drawable.ic_verification_sms,
                MethodItem.getSmsMethodText(phoneNumber.getText().toString())
        ));
        list.add(new MethodItem(
                VerificationActivity.TYPE_PHONE_CALL,
                R.drawable.ic_verification_call,
                MethodItem.getCallMethodText(phoneNumber.getText().toString())
        ));
        return list;
    }

    @Override
    public void showErrorPhoneNumber(String errorMessage) {
        dismissLoading();
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.valueOf(phoneNumber));
        builder.setMessage(getResources().getString(R.string.phone_number_not_registered_info));
        builder.setPositiveButton(getResources().getString(R.string.phone_number_not_registered_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                goToVerifyAccountPage(phoneNumber);
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
        Intent parentIntent = ((TkpdCoreRouter)getActivity().getApplicationContext()).getHomeIntent(getActivity());
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
