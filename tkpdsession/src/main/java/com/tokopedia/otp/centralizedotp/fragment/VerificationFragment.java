package com.tokopedia.otp.centralizedotp.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.centralizedotp.VerificationActivity;
import com.tokopedia.otp.centralizedotp.presenter.VerificationPresenter;
import com.tokopedia.otp.centralizedotp.viewlistener.Verification;
import com.tokopedia.session.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationFragment extends BaseDaggerFragment implements Verification.View {

    private static final long COUNTDOWN_LENGTH = 10000;
    private static final String RESEND = "Kirim ulang";
    private static final String USE_OTHER_METHOD = "gunakan metode verifikasi lain";
    ImageView icon;
    TextView message;
    EditText inputOtp;
    TextView countdownText;
    TextView verifyButton;

    CountDownTimer countDownTimer;
    private boolean isRunningTimer = false;

    @Inject
    VerificationPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new VerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    protected String getScreenName() {
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString
                (VerificationActivity
                        .PARAM_APP_SCREEN, ""))) {
            return getArguments().getString(VerificationActivity.PARAM_APP_SCREEN);
        } else
            return AppScreen.SCREEN_COTP_DEFAULT;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, parent, false);
        icon = view.findViewById(R.id.icon);
        message = view.findViewById(R.id.message);
        inputOtp = view.findViewById(R.id.input_otp);
        countdownText = view.findViewById(R.id.countdown_text);
        verifyButton = view.findViewById(R.id.verify_button);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
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
    }

    private void disableVerifyButton() {
        verifyButton.setEnabled(false);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.grey_button_rounded));
    }

    private void enableVerifyButton() {
        verifyButton.setEnabled(true);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.green_button_rounded));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int imageId = getArguments().getInt(VerificationActivity.PARAM_IMAGE, -1);
        if (imageId != -1)
            ImageHandler.loadImageWithId(icon, imageId);

        message.setText(MethodChecker.fromHtml(getArguments().getString(VerificationActivity
                .PARAM_MESSAGE, "")));

        verifyButton.setEnabled(false);
        presenter.requestOTP(getArguments().getString(VerificationActivity.PARAM_PHONE_NUMBER));
    }

    @Override
    public void onSuccessGetOTP() {
        verifyButton.setEnabled(true);
        startTimer();
    }

    private void startTimer() {
        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(COUNTDOWN_LENGTH, 1000) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds
                            (millisUntilFinished)));
                    Log.d("NISNIS", millisUntilFinished + " " + String.valueOf(TimeUnit
                            .MILLISECONDS.toSeconds(millisUntilFinished)));
                }

                public void onFinish() {
                    isRunningTimer = false;
                    setFinishedCountdownText();
                }

            }.start();
        }
        inputOtp.requestFocus();
    }

    private void setFinishedCountdownText() {
        Spannable spannable = new SpannableString(getString(R.string.countdown_finish_otp));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {
                                  presenter.requestOTP(getArguments().getString
                                          (VerificationActivity.PARAM_PHONE_NUMBER));
                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , spannable.toString().indexOf(RESEND)
                , spannable.toString().lastIndexOf(RESEND) + RESEND.length()
                , 0);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {
                                  goToOtherVerificationMethod();
                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , spannable.toString().indexOf(USE_OTHER_METHOD)
                , spannable.length()
                , 0);

        countdownText.setText(spannable, TextView.BufferType.SPANNABLE);
        countdownText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setRunningCountdownText(String countdown) {
        Spannable spannable = new SpannableString(getString(R.string.please_wait_in)
                + " " +
                countdown
                + " " +
                getString(R.string.to_resend_or_use_other_verif_method));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {
                                  goToOtherVerificationMethod();
                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , spannable.toString().indexOf(USE_OTHER_METHOD)
                , spannable.length()
                , 0);

        countdownText.setText(spannable, TextView.BufferType.SPANNABLE);
        countdownText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void goToOtherVerificationMethod() {
        if (getActivity() instanceof VerificationActivity) {
            ((VerificationActivity) getActivity()).goToSelectVerificationMethod();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
