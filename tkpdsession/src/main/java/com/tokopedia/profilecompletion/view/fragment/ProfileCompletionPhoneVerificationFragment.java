package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel;
import com.tokopedia.session.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.util.CustomPhoneNumberUtil;

/**
 * Created by nisie on 2/22/17.
 */

public class ProfileCompletionPhoneVerificationFragment extends PhoneVerificationFragment {

    public static final String TAG = "verif";
    public static final int RESULT_PROFILE_COMPLETION_PHONE_VERIF = 22;

    private ProfileCompletionContract.View parentView;
    private ProfileCompletionContract.Presenter parentPresenter;

    protected TextView verifyButton;
    private ProfileCompletionViewModel data;
    protected TextView skipFragment;
    private UserSession userSession;

    public ProfileCompletionPhoneVerificationFragment() {

    }

    public static ProfileCompletionPhoneVerificationFragment createInstance
            (ProfileCompletionContract.View view) {
        return new ProfileCompletionPhoneVerificationFragment(view);
    }

    public ProfileCompletionPhoneVerificationFragment(ProfileCompletionContract.View view) {
        this.parentView = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        parentPresenter = parentView.getPresenter();
        userSession = parentView.getUserSession();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        initView();
    }

    @Override
    public void initInjector() {
        super.initInjector();
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        skipButton.setVisibility(View.GONE);
        skipFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentPresenter.skipView(TAG);
            }
        });
    }

    protected void initView() {
        if (parentView == null && getActivity() instanceof ProfileCompletionActivity)
            parentView = ((ProfileCompletionActivity) getActivity())
                    .getProfileCompletionContractView();
        data = parentView.getData();
        verifyButton = (TextView) parentView.getView().findViewById(R.id.proceed);
        verifyButton.setText(getResources().getString(R.string.continue_form));
        verifyButton.setVisibility(View.GONE);

        skipFragment = (TextView) parentView.getView().findViewById(R.id.skip);
        skipFragment.setVisibility(View.VISIBLE);
        skipFragment.setEnabled(true);

        if (data.getPhone() != null) {
            phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(data.getPhone()));
        } else {
            SnackbarManager.make(getActivity(),
                    getString(R.string.please_fill_phone_number),
                    Snackbar.LENGTH_LONG)
                    .show();
        }

        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            phoneNumberEditText.setText(data.getStringExtra(ChangePhoneNumberFragment
                    .EXTRA_PHONE_NUMBER));
        } else if (requestCode == RESULT_PROFILE_COMPLETION_PHONE_VERIF &&
                resultCode == Activity.RESULT_OK) {
            presenter.verifyPhoneNumber(getPhoneNumber());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccessVerifyPhoneNumber() {
        parentView.getUserSession().setIsMsisdnVerified(true);
        userSession.setPhoneNumber(getPhoneNumber());
        parentView.onSuccessEditProfile(EditUserProfileUseCase.EDIT_VERIF);
        CommonUtils.UniversalToast(getActivity(), getString(R.string
                .success_verify_phone_number));
    }


    //    public static final String TAG = "verif";
//    protected static final String FORMAT = "%02d";
//    protected static final long COUNTDOWN_INTERVAL_SECOND = 1000;
//    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
//    private static final String HAS_PHONE_VERIF_TIMER = "HAS_PHONE_VERIF_TIMER";
//    private static final int DEFAULT_COUNTDOWN_TIMER_SECOND = 90;
//    protected TextView verifyButton;
//    protected TextView skipButton;
//    protected TextView phoneNumberEditText;
//    protected TextView changePhoneNumberButton;
//    protected TextView requestOtpButton;
//    protected TextView countdownText;
//    protected TextView requestOtpCallButton;
//    protected View inputOtpView;
//    protected EditText otpEditText;
//    protected CountDownTimer countDownTimer;
//    protected IncomingSmsReceiver smsReceiver;
//    protected TkpdProgressDialog progressDialog;
//    protected LocalCacheHandler cacheHandler;
//
//    @Inject
//    ProfileCompletionPhoneVerificationPresenter presenter;
//
//    private ProfileCompletionViewModel data;
//    private ProfileCompletionContract.View parentView;
//    private ProfileCompletionContract.Presenter parentPresenter;
//    private View instruction;
//
//    public ProfileCompletionPhoneVerificationFragment(ProfileCompletionContract.View view) {
//        this.parentView = view;
//    }
//
//    public ProfileCompletionPhoneVerificationFragment() {
//
//    }
//
//    public static ProfileCompletionPhoneVerificationFragment createInstance
//            (ProfileCompletionContract.View view) {
//        return new
//                ProfileCompletionPhoneVerificationFragment(view);
//    }
//
//    @Override
//    protected String getScreenName() {
//        return AppScreen.SCREEN_PHONE_VERIFICATION;
//    }
//
//    @Override
//    protected void initInjector() {
//        AppComponent appComponent = getComponent(AppComponent.class);
//
//        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
//                DaggerSessionComponent.builder()
//                        .appComponent(appComponent)
//                        .build();
//
//
//        daggerSessionComponent.inject(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
// Bundle savedInstanceState) {
//        View parentView = inflater.inflate(R.layout.fragment_profile_phone_verif_completion,
// container, false);
//        initView(parentView);
//        initialVar();
//        onFirstTimeLaunched();
//        setViewListener();
//        presenter.attachView(this);
//        return parentView;
//    }
//
//    protected void findView(View view) {
//        verifyButton = (TextView) view.findViewById(R.id.verify_button);
//        skipButton = (TextView) view.findViewById(R.id.skip_button);
//        phoneNumberEditText = (TextView) view.findViewById(R.id.phone_number);
//        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);
//        requestOtpButton = (TextView) view.findViewById(R.id.send_otp);
//        requestOtpCallButton = (TextView) view.findViewById(R.id.send_otp_call);
//        countdownText = (TextView) view.findViewById(R.id.countdown_text);
//        inputOtpView = view.findViewById(R.id.input_otp_view);
//        otpEditText = (EditText) view.findViewById(R.id.input_otp);
//        instruction = view.findViewById(R.id.verification_instruction);
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (smsReceiver == null)
//            initSMSReceiver();
//        smsReceiver.registerSmsReceiver(getActivity());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            showCheckSMSPermission();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @TargetApi(Build.VERSION_CODES.M)
//    private void showCheckSMSPermission() {
//            if (ContextCompat.checkSelfPermission(getActivity(),
//                    Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED
//                    && !getActivity().shouldShowRequestPermissionRationale(Manifest.permission
// .RECEIVE_SMS)) {
//                new android.support.v7.app.AlertDialog.Builder(getActivity())
//                        .setMessage(
//                                RequestPermissionUtil
//                                        .getNeedPermissionMessage(Manifest.permission.RECEIVE_SMS)
//                        )
//                        .setPositiveButton(com.tokopedia.core.R.string.title_ok, new
// DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ProfileCompletionPhoneVerificationFragmentPermissionsDispatcher
//                                        .checkSmsPermissionWithCheck
// (ProfileCompletionPhoneVerificationFragment.this);
//
//                            }
//                        })
//                        .setNegativeButton(com.tokopedia.core.R.string.dialog_cancel, new
// DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                RequestPermissionUtil.onPermissionDenied(MainApplication
// .getAppContext(),
//                                        Manifest.permission.RECEIVE_SMS);
//                            }
//                        })
//                        .show();
//            } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission
// .RECEIVE_SMS)) {
//                ProfileCompletionPhoneVerificationFragmentPermissionsDispatcher
//                        .checkSmsPermissionWithCheck(ProfileCompletionPhoneVerificationFragment
// .this);
//            }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (smsReceiver != null)
//            getActivity().unregisterReceiver(smsReceiver);
//    }
//
//    protected void onFirstTimeLaunched() {
//        phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(
//                SessionHandler.getPhoneNumber()));
//    }
//
//    protected void initView(View view) {
//        if (parentView == null && getActivity() instanceof ProfileCompletionActivity)
//            parentView = ((ProfileCompletionActivity) getActivity())
//                    .getProfileCompletionContractView();
//        findView(view);
//        data = parentView.getData();
//        verifyButton = (TextView) parentView.getView().findViewById(R.id.proceed);
//        verifyButton.setText(getResources().getString(R.string.continue_form));
//        parentView.canProceed(false);
//
//        skipButton = (TextView) parentView.getView().findViewById(R.id.skip);
//
//        if(data.getPhone() != null) {
//            phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(data.getPhone()));
//        } else {
//            SnackbarManager.make(getActivity(),
//                    getString(R.string.please_fill_phone_number),
//                    Snackbar.LENGTH_LONG)
//                    .show();
//        }
//
//
//        KeyboardHandler.DropKeyboard(getActivity(), getView());
//
//        Spannable spannable = new SpannableString(getString(R.string.via_call));
//
//        spannable.setSpan(new ClickableSpan() {
//                              @Override
//                              public void onClick(View view) {
//
//                              }
//
//                              @Override
//                              public void updateDrawState(TextPaint ds) {
//                                  ds.setColor(MethodChecker.getColor(getActivity(),
//                                          com.tokopedia.core.R.color.tkpd_main_green));
//                              }
//                          }
//                , getString(R.string.via_call).indexOf(getString(R.string.via_call_short))
//                , getString(R.string.via_call).indexOf(getString(R.string.via_call_short))
//                        + getString(R.string.via_call_short).length()
//                , 0);
//
//        requestOtpCallButton.setText(spannable, TextView.BufferType.SPANNABLE);
//    }
//
//
//    protected void setViewListener() {
//        verifyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                presenter.verifyPhoneNumber(getOTPCode(), getPhoneNumber());
//            }
//        });
//        requestOtpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.requestOtp();
//            }
//        });
//
//        requestOtpCallButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                presenter.requestOtpWithCall();
//
//            }
//        });
//        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(
//                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
//                                getActivity(),
//                                phoneNumberEditText.getText().toString()),
//                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
//            }
//        });
//
//        otpEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 6) {
//                    parentView.canProceed(true);
//
//                } else {
//                    parentView.canProceed(false);
//                }
//            }
//        });
//
//        skipButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                parentPresenter.skipView(TAG);
//            }
//        });
//    }
//
//    protected void initialVar() {
//        initSMSReceiver();
//        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_PHONE_VERIF_TIMER);
//        parentPresenter = parentView.getPresenter();
//    }
//
//    private void initSMSReceiver() {
//        this.smsReceiver = new IncomingSmsReceiver();
//        this.smsReceiver.setListener(this);
//    }
//
//    @Override
//    public void onSuccessRequestOtp(String status) {
//        finishProgressDialog();
//        SnackbarManager.make(getActivity(), status, Snackbar.LENGTH_LONG).show();
//        inputOtpView.setVisibility(View.VISIBLE);
//        changePhoneNumberButton.setVisibility(View.GONE);
//        setViewEnabled(true);
//        startTimer();
//    }
//
//    @Override
//    public String getPhoneNumber() {
//        return phoneNumberEditText.getText().toString().replace("-", "");
//    }
//
//    @Override
//    public void onErrorRequestOTP(String errorMessage) {
//        setViewEnabled(true);
//        finishProgressDialog();
//        if (errorMessage.equals(""))
//            NetworkErrorHelper.showSnackbar(getActivity());
//        else
//            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
//    }
//
//    protected void finishProgressDialog() {
//        if (progressDialog != null)
//            progressDialog.dismiss();
//    }
//
//    @Override
//    public void showProgressDialog() {
//        if (progressDialog == null)
//            progressDialog = new TkpdProgressDialog(getActivity(),
//                    TkpdProgressDialog.NORMAL_PROGRESS);
//
//        if (getActivity() != null)
//            progressDialog.showDialog();
//    }
//
//    @Override
//    public void onSuccessVerifyPhoneNumber() {
//        finishProgressDialog();
//        setViewEnabled(true);
//        SessionHandler.setIsMSISDNVerified(true);
//        verifyButton.setText(getResources().getString(R.string.continue_form));
//        SessionHandler.setPhoneNumber(phoneNumberEditText.getText().toString().replace("-", ""));
//        parentView.onSuccessEditProfile(EditUserProfileUseCase.EDIT_VERIF);
//        CommonUtils.UniversalToast(getActivity(), getString(R.string
// .success_verify_phone_number));
//    }
//
//    @Override
//    public void showErrorPhoneNumber(String errorMessage) {
//        phoneNumberEditText.setError(errorMessage);
//
//    }
//
//    @Override
//    public String getOTPCode() {
//        return otpEditText.getText().toString();
//    }
//
//    @Override
//    public void onErrorVerifyPhoneNumber(String errorMessage) {
//        setViewEnabled(true);
//        finishProgressDialog();
//        if (errorMessage.equals(""))
//            NetworkErrorHelper.showSnackbar(getActivity());
//        else
//            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
//    }
//
//    @Override
//    public void setViewEnabled(boolean isEnabled) {
//        requestOtpButton.setEnabled(isEnabled);
//        requestOtpCallButton.setEnabled(isEnabled);
//        if (inputOtpView.getVisibility() == View.VISIBLE)
//            verifyButton.setEnabled(isEnabled);
//        if (inputOtpView.getVisibility() == View.VISIBLE) {
//            verifyButton.setText(getResources().getString(R.string.title_confirm_otp));
//        } else {
//            verifyButton.setText(getResources().getString(R.string.continue_form));
//        }
//        skipButton.setEnabled(isEnabled);
//    }
//
//    protected void startTimer() {
//        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
//            cacheHandler.putBoolean(HAS_PHONE_VERIF_TIMER, true);
//            cacheHandler.setExpire(DEFAULT_COUNTDOWN_TIMER_SECOND);
//            cacheHandler.applyEditor();
//        }
//
//        runAnimation();
//        if (inputOtpView.getVisibility() == View.VISIBLE) {
//            verifyButton.setText(getResources().getString(R.string.title_confirm_otp));
//        } else {
//            verifyButton.setText(getResources().getString(R.string.continue_form));
//        }
//        otpEditText.requestFocus();
//    }
//
//    protected void stopTimer() {
//        instruction.setVisibility(View.VISIBLE);
//        requestOtpButton.setVisibility(View.VISIBLE);
//        inputOtpView.setVisibility(View.GONE);
//        verifyButton.setText(getResources().getString(R.string.continue_form));
//        parentView.canProceed(false);
//    }
//
//    protected void runAnimation() {
//        countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000,
// COUNTDOWN_INTERVAL_SECOND) {
//            public void onTick(long millisUntilFinished) {
//                instruction.setVisibility(GONE);
//                countdownText.setVisibility(View.VISIBLE);
//                MethodChecker.setBackground(countdownText, MethodChecker.getDrawable
// (getActivity(), R.drawable.prof_comp_bg_textview_rounded_white));
//                countdownText.setText(
//                        "Kirim SMS Ulang (" + String.format(FORMAT,
//                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))
//                                + ")");
//                requestOtpCallButton.setVisibility(GONE);
//            }
//
//            public void onFinish() {
//                enableOtpButton();
//            }
//
//        }.start();
//    }
//
//    protected void enableOtpButton() {
//        instruction.setVisibility(View.VISIBLE);
//        countdownText.setVisibility(GONE);
//        requestOtpButton.setVisibility(View.VISIBLE);
//        requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
//        requestOtpButton.setText(R.string.title_resend_otp_sms);
//        requestOtpCallButton.setVisibility(View.VISIBLE);
//        changePhoneNumberButton.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
//                resultCode == Activity.RESULT_OK) {
//            phoneNumberEditText.setText(data.getStringExtra(ChangePhoneNumberFragment
// .EXTRA_PHONE_NUMBER));
//            stopTimer();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
//        presenter.detachView();
//        cacheHandler = null;
//    }
//
//    @Override
//    public void onReceiveOTP(String otpCode) {
//        processOTPSMS(otpCode);
//    }
//
//
//    @NeedsPermission(Manifest.permission.RECEIVE_SMS)
//    public void processOTPSMS(String otpCode) {
//        if (otpEditText != null)
//            otpEditText.setText(otpCode);
//        presenter.verifyPhoneNumber(otpCode, getPhoneNumber());
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
// grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        ProfileCompletionPhoneVerificationFragmentPermissionsDispatcher
// .onRequestPermissionsResult(
//                ProfileCompletionPhoneVerificationFragment.this, requestCode, grantResults);
//    }
//
//    @OnShowRationale(Manifest.permission.RECEIVE_SMS)
//    void showRationaleForReadSms(final PermissionRequest request) {
//        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission
// .RECEIVE_SMS);
//    }
//
//    @OnPermissionDenied(Manifest.permission.RECEIVE_SMS)
//    void showDeniedForReadSms() {
//        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.RECEIVE_SMS);
//    }
//
//    @OnNeverAskAgain(Manifest.permission.RECEIVE_SMS)
//    void showNeverAskForReadSms() {
//        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.RECEIVE_SMS);
//    }
//
//    @NeedsPermission(Manifest.permission.RECEIVE_SMS)
//    public void checkSmsPermission() {
//
//    }

}
