package com.tokopedia.otp.tokocashotp.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.presenter.VerificationPresenter;
import com.tokopedia.otp.tokocashotp.view.viewlistener.Verification;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.view.activity.ChooseTokocashAccountActivity;
import com.tokopedia.session.login.loginphonenumber.view.activity.NotConnectedTokocashActivity;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.ChooseTokoCashAccountViewModel;
import com.tokopedia.session.login.view.customview.LargerSpannedMovementMethod;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationFragment extends BaseDaggerFragment implements Verification.View {

    private static final String ARGS_DATA = "ARGS_DATA";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;
    private static final String RESEND = "Kirim ulang";
    private static final String USE_OTHER_METHOD = "gunakan metode verifikasi lain";

    private static final String CACHE_OTP = "CACHE_OTP";
    private static final String HAS_TIMER = "has_timer";

    ImageView icon;
    TextView message;
    PinEntryEditText inputOtp;
    TextView countdownText;
    TextView verifyButton;
    TextView errorOtp;
    View limitOtp;

    CountDownTimer countDownTimer;
    TkpdProgressDialog progressDialog;

    private boolean isRunningTimer = false;
    protected LocalCacheHandler cacheHandler;
    private VerificationViewModel viewModel;

    @Inject
    VerificationPresenter presenter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new VerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(ARGS_DATA);
        } else if (getArguments() != null) {
            viewModel = createViewModel(getArguments());
        } else {
            getActivity().finish();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_OTP);
    }

    private VerificationViewModel createViewModel(Bundle bundle) {
        return new VerificationViewModel(
                bundle.getInt(VerificationActivity.PARAM_FRAGMENT_TYPE, -1),
                bundle.getInt(VerificationActivity.PARAM_IMAGE, -1),
                bundle.getString(VerificationActivity.PARAM_MESSAGE, ""),
                bundle.getString(VerificationActivity.PARAM_PHONE_NUMBER, ""),
                bundle.getString(VerificationActivity.PARAM_APP_SCREEN, "")
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, viewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    protected String getScreenName() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getAppScreen())) {
            return viewModel.getAppScreen();
        } else
            return AppScreen.SCREEN_COTP_DEFAULT;
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
        limitOtp = view.findViewById(R.id.limit_otp);
        errorOtp = view.findViewById(R.id.error_otp);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_TIMER, false)) {
            startTimer();
        } else {
            setLimitReachedCountdownText();
        }

        limitOtp.setVisibility(View.GONE);
        countdownText.setHighlightColor(Color.TRANSPARENT);

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

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.verifyOtp(viewModel.getPhoneNumber(), inputOtp.getText().toString());
            }
        });
    }

    private void disableVerifyButton() {
        verifyButton.setEnabled(false);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.grey_button_rounded));
        inputOtp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void enableVerifyButton() {
        verifyButton.setEnabled(true);
        verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        MethodChecker.setBackground(verifyButton, MethodChecker.getDrawable(getActivity(), R
                .drawable.green_button_rounded));
        inputOtp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        errorOtp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        int imageId = viewModel.getIconResId();
        ImageHandler.loadImageWithId(icon, imageId);
        message.setText(MethodChecker.fromHtml(viewModel.getMessage()));
        verifyButton.setEnabled(false);
        presenter.requestOTP(viewModel);
    }

    @Override
    public void onSuccessGetOTP() {
        verifyButton.setEnabled(true);
        startTimer();
    }

    @Override
    public void onSuccessVerifyOTP(VerifyOtpTokoCashViewModel verifyOtpTokoCashViewModel) {
        resetCountDown();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChooseTokocashAccountActivity.ARGS_DATA,
                new ChooseTokoCashAccountViewModel(verifyOtpTokoCashViewModel.getList(),
                        viewModel.getPhoneNumber(),
                        verifyOtpTokoCashViewModel.getKey()));
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void resetCountDown() {
        cacheHandler.putBoolean(HAS_TIMER, false);
        cacheHandler.applyEditor();
    }

    @Override
    public void onErrorGetOTP(String errorMessage) {
        if (errorMessage.contains(getString(R.string.limit_otp_reached))) {
            limitOtp.setVisibility(View.VISIBLE);
            setLimitReachedCountdownText();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
            setFinishedCountdownText();
        }
    }

    @Override
    public void onErrorVerifyOtp(String errorMessage) {
        inputOtp.setError(true);
        errorOtp.setVisibility(View.VISIBLE);
        inputOtp.setCompoundDrawables(null, null, MethodChecker.getDrawable
                (getActivity(), R.drawable.ic_cancel_red), null);
    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        if (!progressDialog.isProgress())
            progressDialog.showDialog();
    }

    @Override
    public void dismissLoadingProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onErrorNoAccountTokoCash() {
        startActivity(NotConnectedTokocashActivity.getNoTokocashAccountIntent(getActivity(),
                viewModel.getPhoneNumber()));
        getActivity().finish();
    }

    @Override
    public boolean isCountdownFinished() {
        return cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_TIMER, false);
    }

    private void startTimer() {
        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_TIMER, false)) {
            cacheHandler.putBoolean(HAS_TIMER, true);
            cacheHandler.setExpire(COUNTDOWN_LENGTH);
            cacheHandler.applyEditor();
        }

        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * INTERVAL, INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds
                            (millisUntilFinished)));
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
                                  presenter.requestOTP(viewModel);
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
        countdownText.setMovementMethod(new LargerSpannedMovementMethod());

    }

    private void setLimitReachedCountdownText() {
        Spannable spannable = new SpannableString(getString(R.string.login_with_other_method));

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
                , spannable.toString().indexOf(getString(R.string.login_with_other_method))
                , spannable.length()
                , 0);

        countdownText.setText(spannable, TextView.BufferType.SPANNABLE);
        countdownText.setMovementMethod(new LargerSpannedMovementMethod());
    }

    private void setRunningCountdownText(String countdown) {
        Spannable spannable = new SpannableString(getString(R.string.please_wait_in)
                + " " +
                countdown
                + " " +
                getString(R.string.to_resend_otp));

        countdownText.setText(spannable, TextView.BufferType.SPANNABLE);
        countdownText.setMovementMethod(new LargerSpannedMovementMethod());
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
        progressDialog = null;

        presenter.detachView();
    }

    public void setData(Bundle bundle) {
        viewModel = createViewModel(bundle);
    }
}
