package com.tokopedia.session.changephonenumber.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.activity.ChangePhoneNumberInputActivity;
import com.tokopedia.session.changephonenumber.view.listener
        .ChangePhoneNumberEmailVerificationFragmentListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by milhamj on 03/01/18.
 */

public class ChangePhoneNumberEmailVerificationFragment extends BaseDaggerFragment
        implements ChangePhoneNumberEmailVerificationFragmentListener.View {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;

    private static final String CACHE_EMAIL_VERIF = "CACHE_EMAIL_VERIF";
    private static final String HAS_TIMER = "has_timer";
    protected LocalCacheHandler cacheHandler;
    @Inject
    ChangePhoneNumberEmailVerificationFragmentListener.Presenter presenter;
    ImageView icon;
    TextView message;
    PinEntryEditText inputOtp;
    TextView countdownText;
    TextView verifyButton;
    TextView errorOtp;
    TextView limitOtpText;
    View limitOtp;
    View finishCountdownView;
    CountDownTimer countDownTimer;
    TkpdProgressDialog progressDialog;
    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;
    private boolean isRunningTimer = false;

    public static ChangePhoneNumberEmailVerificationFragment newInstance(String phoneNumber,
                                                                         String email,
                                                                         ArrayList<String>
                                                                                 warningList) {
        ChangePhoneNumberEmailVerificationFragment fragment = new
                ChangePhoneNumberEmailVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putStringArrayList(PARAM_WARNING_LIST, warningList);
        bundle.putString(PARAM_EMAIL, email);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_EMAIL_VERIF);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout
                .fragment_change_phone_number_email_verification, container, false);
        presenter.attachView(this);
        initVar();
        initView(parentView);
        setViewListener();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareView();
        presenter.initView();
    }

    private void initVar() {
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
        warningList = getArguments().getStringArrayList(PARAM_WARNING_LIST);
        email = getArguments().getString(PARAM_EMAIL);
    }

    private void initView(View view) {
        icon = view.findViewById(R.id.icon);
        message = view.findViewById(R.id.message);
        inputOtp = view.findViewById(R.id.input_otp);
        countdownText = view.findViewById(R.id.countdown_text);
        verifyButton = view.findViewById(R.id.verify_button);
        limitOtp = view.findViewById(R.id.limit_otp);
        errorOtp = view.findViewById(R.id.error_otp);
        finishCountdownView = view.findViewById(R.id.finish_countdown);
        limitOtpText = view.findViewById(R.id.limit_otp_text);
    }

    private void setViewListener() {
        inputOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputOtp.getText().length() == 6) {
                    enableVerifyButton();
                } else {
                    disableVerifyButton();
                }
            }
        });

        inputOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && inputOtp.length() == 6) {
                    presenter.validateOtp(inputOtp.getText().toString());
                    return true;
                }
                return false;
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.validateOtp(inputOtp.getText().toString());
            }
        });
    }

    private void prepareView() {
        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_TIMER, false)) {
            startTimer();
        } else {
            setLimitReachedCountdownText();
        }

        String text = String.format("%s <b>%s</b>.",
                getString(R.string.verification_code_email_sent_to),
                email);
        message.setText(MethodChecker.fromHtml(text));
        limitOtp.setVisibility(View.GONE);
    }

    private void disableVerifyButton() {
        verifyButton.setEnabled(false);
        verifyButton.setTextColor(MethodChecker.getColor(getContext(), R.color.grey_500));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getContext(), R
                .drawable.grey_button_rounded));
        inputOtp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void enableVerifyButton() {
        verifyButton.setEnabled(true);
        verifyButton.setTextColor(MethodChecker.getColor(getContext(), R.color.white));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getContext(), R
                .drawable.green_button_rounded));
        inputOtp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        errorOtp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_COTP_EMAIL;
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getContext(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        if (!progressDialog.isProgress())
            progressDialog.showDialog();
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        SessionComponent sessionComponent =
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .sessionModule(new SessionModule())
                        .build();
        sessionComponent.inject(this);
    }

    @Override
    public void onSendEmailSuccess() {
        startTimer();
    }

    @Override
    public void onSendEmailError(String message) {
        if (message.contains(getString(R.string.limit_otp_email_reached))) {
            limitOtpText.setText(message);
            limitOtp.setVisibility(View.VISIBLE);
            setLimitReachedCountdownText();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
            setFinishedCountdownText();
        }
    }

    @Override
    public void onValidateOtpSuccess() {
        goToNextActivity();
    }

    @Override
    public void onValidateOtpError(String message) {
        inputOtp.setError(true);
        errorOtp.setVisibility(View.VISIBLE);
        inputOtp.setCompoundDrawables(null, null, MethodChecker.getDrawable
                (getContext(), R.drawable.ic_cancel_red), null);
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getContext(), getView());
    }

    private void startTimer() {
        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_TIMER, false)) {
            cacheHandler.putBoolean(HAS_TIMER, true);
            cacheHandler.setExpire(COUNTDOWN_LENGTH);
            cacheHandler.applyEditor();
        }

        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * INTERVAL,
                    INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    if (getContext() != null)
                        setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds
                            (millisUntilFinished)));
                }

                public void onFinish() {
                    isRunningTimer = false;
                    if (getContext() != null)
                        setFinishedCountdownText();
                }

            }.start();
        }
        inputOtp.requestFocus();
    }

    private void setFinishedCountdownText() {
        countdownText.setVisibility(View.GONE);
        finishCountdownView.setVisibility(View.VISIBLE);

        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendEmail();
            }
        });
    }

    private void setLimitReachedCountdownText() {

        finishCountdownView.setVisibility(View.GONE);
        countdownText.setVisibility(View.GONE);
    }

    private void setRunningCountdownText(String countdown) {
        countdownText.setVisibility(View.VISIBLE);
        finishCountdownView.setVisibility(View.GONE);

        countdownText.setTextColor(MethodChecker.getColor(getContext(), R.color.black_38));

        String text = String.format("%s <b> %s %s</b> %s",
                getString(R.string.please_wait_in),
                countdown,
                getString(R.string.second),
                getString(R.string.to_resend_otp));

        countdownText.setText(MethodChecker.fromHtml(text));

    }

    private void goToNextActivity() {
        Intent intent = ChangePhoneNumberInputActivity.newInstance(getContext(), phoneNumber,
                email, warningList);
        intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        getActivity().finish();
    }
}
