package com.tokopedia.session.addchangeemail.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.PinEntryEditText;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.addchangeemail.view.activity.AddEmailVerificationActivity;
import com.tokopedia.session.addchangeemail.view.listener.AddEmailVerificationListener;
import com.tokopedia.session.addchangeemail.view.presenter.AddEmailVerificationPresenter;
import com.tokopedia.session.addchangeemail.view.viewmodel.VerificationModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddEmailVerificationFragment extends BaseDaggerFragment implements AddEmailVerificationListener.View {


    public static final String PARAM_FRAGMENT_TYPE = "type";
    public static final String PARAM_APP_SCREEN = "app_screen";
    public static final String PARAM_METHOD_LIST = "method_list";
    private static final String ARGS_DATA = "ARGS_DATA";

    private static final int COUNTDOWN_LENGTH = 90;
    private static final int INTERVAL = 1000;
    private static final int MAX_INPUT_OTP = 5;


    private static final String CACHE_OTP = "CACHE_OTP";
    private static final String HAS_TIMER = "has_timer";

    ImageView icon;
    TextView message;
    PinEntryEditText inputOtp;
    TextView countdownText;
    TextView verifyButton;
    TextView errorOtp;
    View limitOtp;
    View finishCountdownView;
    TextView noCodeText;
    ImageView errorImage;

    CountDownTimer countDownTimer;
    TkpdProgressDialog progressDialog;

    private boolean isRunningTimer = false;
    protected LocalCacheHandler cacheHandler;
    private VerificationModel viewModel;

    public static AddEmailVerificationFragment newInstance(Bundle bundle) {
        AddEmailVerificationFragment fragment = new AddEmailVerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    AddEmailVerificationPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_email_verification, container, false);
        icon = view.findViewById(R.id.icon);
        message = (TextView)view.findViewById(R.id.message);
        inputOtp = view.findViewById(R.id.input_otp);
        countdownText = (TextView) view.findViewById(R.id.countdown_text);
        verifyButton = (TextView)view.findViewById(R.id.verify_button);
        limitOtp = view.findViewById(R.id.limit_otp);
        errorOtp = (TextView)view.findViewById(R.id.error_otp);
        finishCountdownView = view.findViewById(R.id.finish_countdown);
        noCodeText = (TextView)view.findViewById(R.id.no_code);
        errorImage = view.findViewById(R.id.error_image);
        prepareView();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.v("yfsx", "saveInstance");
            viewModel = savedInstanceState.getParcelable(ARGS_DATA);
        } else if (getArguments() != null) {
            Log.v("yfsx", "parcelable");
            viewModel = createViewModel(getArguments());
        } else {
            getActivity().finish();
        }

        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_OTP);
    }

    private VerificationModel createViewModel(Bundle bundle) {
        return new VerificationModel(
                bundle.getInt(AddEmailVerificationActivity.PARAM_IMAGE, -1),
                bundle.getString(AddEmailVerificationActivity.PARAM_MESSAGE, ""),
                bundle.getString(AddEmailVerificationActivity.PARAM_EMAIL, "")
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, viewModel);
    }

    private void initData() {
        int imageId = viewModel.getIconResId();
        ImageHandler.loadImageWithId(icon, imageId);
        message.setText(MethodChecker.fromHtml(viewModel.getMessage()));
        verifyButton.setEnabled(false);
        presenter.sendRequest(viewModel.getEmail());
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
                if (inputOtp.getText().length() == MAX_INPUT_OTP) {
                    enableNextButton();
                } else {
                    disableNextButton();
                }
            }
        });

        inputOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && inputOtp.length() == MAX_INPUT_OTP) {
                    presenter.sendVerify(viewModel.getEmail(), inputOtp.getText().toString());
                    return true;
                }
                return false;
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendVerify(viewModel.getEmail(), inputOtp.getText().toString());
            }
        });

        errorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOtp.setText("");
                removeErrorOtp();
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void enableNextButton() {
        enableButton(verifyButton);
    }

    @Override
    public void disableNextButton() {
        disableButton(verifyButton);
    }

    @Override
    public void onErrorRequest(String error) {
        if (error.contains(getString(R.string.limit_otp_reached))) {
            limitOtp.setVisibility(View.VISIBLE);
            setLimitReachedCountdownText();
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), error);
            setFinishedCountdownText();
        }
    }

    @Override
    public void onSuccessRequest() {
        startTimer();
    }

    @Override
    public void onErrorVerify(String error) {
        dismissLoading();
        inputOtp.setError(true);
        inputOtp.setFocusableInTouchMode(true);
        inputOtp.post(new Runnable() {
            public void run() {
                inputOtp.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(inputOtp, 0);
            }
        });
        errorImage.setVisibility(View.VISIBLE);
        errorOtp.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessVerify() {
        dismissLoading();
        removeErrorOtp();
        resetCountDown();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void resetCountDown() {
        cacheHandler.putBoolean(HAS_TIMER, false);
        cacheHandler.applyEditor();
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
        countdownText.setOnClickListener(null);
        if (isCountdownFinished()) {
            cacheHandler.putBoolean(HAS_TIMER, true);
            cacheHandler.setExpire(COUNTDOWN_LENGTH);
            cacheHandler.applyEditor();
        }

        if (!isRunningTimer) {
            countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * INTERVAL, INTERVAL) {
                public void onTick(long millisUntilFinished) {
                    isRunningTimer = true;
                    if (isAdded())
                        setRunningCountdownText(String.valueOf(TimeUnit.MILLISECONDS
                                .toSeconds(millisUntilFinished)));
                }

                public void onFinish() {
                    isRunningTimer = false;
                    setFinishedCountdownText();
                }

            }.start();
        }
        inputOtp.requestFocus();
    }


    private void setRunningCountdownText(String countdown) {
        countdownText.setVisibility(View.VISIBLE);
        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_38));

        String text = String.format("%s <b> %s %s</b> %s",
                getString(R.string.please_wait_in),
                countdown,
                getString(R.string.second),
                getString(R.string.to_resend_otp));

        countdownText.setText(MethodChecker.fromHtml(text));

    }

    private void setFinishedCountdownText() {
        countdownText.setVisibility(View.GONE);
        finishCountdownView.setVisibility(View.VISIBLE);
        noCodeText.setVisibility(View.VISIBLE);

        TextView resend = finishCountdownView.findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOtp.setText("");
                removeErrorOtp();
                presenter.sendRequest(viewModel.getEmail());
            }
        });
    }

    private void setLimitReachedCountdownText() {
        countdownText.setVisibility(View.VISIBLE);
        finishCountdownView.setVisibility(View.GONE);
        noCodeText.setVisibility(View.GONE);

        countdownText.setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        countdownText.setText(R.string.login_with_other_method);
        countdownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOtherVerificationMethod();
            }
        });
//        getActivity().setResult(Activity.RESULT_CANCELED);
//        getActivity().finish();
    }
    private void goToOtherVerificationMethod() {

    }

    private void removeErrorOtp() {
        inputOtp.setError(false);
        errorOtp.setVisibility(View.INVISIBLE);
        errorImage.setVisibility(View.INVISIBLE);
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

    private void enableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(true);
    }
}
