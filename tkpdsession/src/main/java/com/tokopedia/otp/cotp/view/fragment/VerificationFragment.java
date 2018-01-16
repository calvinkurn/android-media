package com.tokopedia.otp.cotp.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
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
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.presenter.VerificationPresenter;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.session.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationFragment extends BaseDaggerFragment implements Verification.View {

    private static final String ARGS_DATA = "ARGS_DATA";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;
    private static final int MAX_OTP_LENGTH = 6;

    private static final String CACHE_OTP = "CACHE_OTP";
    private static final String HAS_TIMER = "has_timer";

    private static final int REQUEST_VERIFY_PHONE_NUMBER = 243;

    private ImageView icon;
    private TextView message;
    private PinEntryEditText inputOtp;
    private TextView countdownText;
    private TextView verifyButton;
    private TextView errorOtp;
    private View limitOtp;
    private View finishCountdownView;
    private TextView noCodeText;

    private CountDownTimer countDownTimer;
    private TkpdProgressDialog progressDialog;

    private boolean isRunningTimer = false;
    protected LocalCacheHandler cacheHandler;
    private VerificationViewModel viewModel;
    private VerificationPassModel verificationPassModel;

    @Inject
    VerificationPresenter presenter;

    @Inject
    GlobalCacheManager globalCacheManager;

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

        if (getArguments() != null) {
            viewModel = createViewModel(getArguments());
        } else {
            getActivity().finish();
        }

        if (globalCacheManager != null && globalCacheManager.getConvertObjData(VerificationActivity.PASS_MODEL,
                VerificationPassModel.class) != null) {
            verificationPassModel = globalCacheManager.getConvertObjData(VerificationActivity.PASS_MODEL,
                    VerificationPassModel.class);
        } else {
            getActivity().finish();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_OTP);
    }

    private VerificationViewModel createViewModel(Bundle bundle) {
        return new VerificationViewModel(
                bundle.getInt(VerificationActivity.PARAM_DEFAULT_FRAGMENT_TYPE, -1),
                bundle.getInt(VerificationActivity.PARAM_IMAGE, -1),
                bundle.getString(VerificationActivity.PARAM_MESSAGE, ""),
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
        finishCountdownView = view.findViewById(R.id.finish_countdown);
        noCodeText = view.findViewById(R.id.no_code);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    private void prepareView() {
        if (!isCountdownFinished()) {
            startTimer();
        } else {
            setLimitReachedCountdownText();
        }

        limitOtp.setVisibility(View.GONE);
        inputOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputOtp.getText().length() == MAX_OTP_LENGTH) {
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
                        && inputOtp.length() == MAX_OTP_LENGTH) {
                    presenter.verifyOtp(verificationPassModel, inputOtp.getText().toString());
                    return true;
                }
                return false;
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.verifyOtp(verificationPassModel, inputOtp.getText().toString());
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
        presenter.requestOTP(viewModel, verificationPassModel);
    }

    @Override
    public void onSuccessGetOTP() {
        verifyButton.setEnabled(true);
        startTimer();
    }

    @Override
    public void onSuccessVerifyOTP() {
        resetCountDown();
        getActivity().setResult(Activity.RESULT_OK);
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
    public boolean isCountdownFinished() {
        return cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_TIMER, false);
    }

    @Override
    public void dropKeyboard() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    private void startTimer() {
        if (isCountdownFinished()) {
            cacheHandler.putBoolean(HAS_TIMER, true);
            cacheHandler.setExpire(COUNTDOWN_LENGTH);
            cacheHandler.applyEditor();
        }

        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * INTERVAL, INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    if (getActivity() != null)
                        setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS.toSeconds
                                (millisUntilFinished)));
                }

                public void onFinish() {
                    isRunningTimer = false;
                    if (getActivity() != null)
                        setFinishedCountdownText();
                }

            }.start();
        }
        inputOtp.requestFocus();
    }

    private void setFinishedCountdownText() {
        countdownText.setVisibility(View.GONE);
        finishCountdownView.setVisibility(View.VISIBLE);
        noCodeText.setVisibility(View.VISIBLE);

        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOTP(viewModel, verificationPassModel);
            }
        });

        TextView useOtherMethod = finishCountdownView.findViewById(R.id.use_other_method);
        TextView or = finishCountdownView.findViewById(R.id.or);

        if (verificationPassModel != null
                && verificationPassModel.getListAvailableMethods().size() > 1) {
            or.setVisibility(View.VISIBLE);
            useOtherMethod.setVisibility(View.VISIBLE);

            useOtherMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropKeyboard();
                    goToOtherVerificationMethod();
                }
            });
        } else {
            or.setVisibility(View.GONE);
            useOtherMethod.setVisibility(View.GONE);
        }
    }

    private void setLimitReachedCountdownText() {

        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        if (verificationPassModel != null
                && verificationPassModel.getListAvailableMethods().size() > 1) {
            countdownText.setVisibility(View.VISIBLE);
            countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
            countdownText.setText(R.string.login_with_other_method);
            countdownText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToOtherVerificationMethod();
                }
            });
        } else {
            countdownText.setVisibility(View.GONE);
        }

    }

    private void setRunningCountdownText(String countdown) {
        countdownText.setVisibility(View.VISIBLE);
        countdownText.setOnClickListener(null);
        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_38));
        countdownText.setEnabled(false);
        String text = String.format("%s <b> %s %s</b> %s",
                getString(R.string.please_wait_in),
                countdown,
                getString(R.string.second),
                getString(R.string.to_resend_otp));

        countdownText.setText(MethodChecker.fromHtml(text));

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
