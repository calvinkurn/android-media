package com.tokopedia.otp.phoneverification.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.activity.ChangePhoneNumberActivity;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenter;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenterImpl;
import com.tokopedia.session.R;

import java.util.concurrent.TimeUnit;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by nisie on 2/22/17.
 */

@RuntimePermissions
public class PhoneVerificationFragment extends BasePresenterFragment<PhoneVerificationPresenter>
        implements PhoneVerificationFragmentView, IncomingSmsReceiver.ReceiveSMSListener {

    public interface PhoneVerificationFragmentListener {
        void onSkipVerification();

        void onSuccessVerification();
    }

    private static final String FORMAT = "%02d";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String HAS_PHONE_VERIF_TIMER = "HAS_PHONE_VERIF_TIMER";
    private static final int DEFAULT_COUNTDOWN_TIMER_SECOND = 90;
    private static final long COUNTDOWN_INTERVAL_SECOND = 1000;

    TextView verifyButton;
    TextView skipButton;
    TextView phoneNumberEditText;
    TextView changePhoneNumberButton;
    TextView requestOtpButton;
    TextView countdownText;
    TextView requestOtpCallButton;
    View inputOtpView;
    EditText otpEditText;

    CountDownTimer countDownTimer;
    IncomingSmsReceiver smsReceiver;
    TkpdProgressDialog progressDialog;
    LocalCacheHandler cacheHandler;
    PhoneVerificationFragmentListener listener;

    public static PhoneVerificationFragment createInstance(PhoneVerificationFragmentListener listener) {
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        fragment.setPhoneVerificationListener(listener);
        return fragment;
    }

    public PhoneVerificationFragment() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);
    }

    public void setPhoneVerificationListener(PhoneVerificationFragmentListener listener) {
        this.listener = listener;
    }

    public PhoneVerificationFragmentListener getListener() {
        return listener;
    }

    private void findView(View view) {
        verifyButton = (TextView) view.findViewById(R.id.verify_button);
        skipButton = (TextView) view.findViewById(R.id.skip_button);
        phoneNumberEditText = (TextView) view.findViewById(R.id.phone_number);
        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);
        requestOtpButton = (TextView) view.findViewById(R.id.send_otp);
        requestOtpCallButton = (TextView) view.findViewById(R.id.send_otp_call);
        countdownText = (TextView) view.findViewById(R.id.countdown_text);
        inputOtpView = view.findViewById(R.id.input_otp_view);
        otpEditText = (EditText) view.findViewById(R.id.input_otp);

    }

    @Override
    public void onResume() {
        super.onResume();
        smsReceiver.registerSmsReceiver(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showCheckSMSPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @TargetApi(Build.VERSION_CODES.M)
    private void showCheckSMSPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED
                && !getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            new android.support.v7.app.AlertDialog.Builder(getActivity())
                    .setMessage(
                            RequestPermissionUtil
                                    .getNeedPermissionMessage(Manifest.permission.READ_SMS)
                    )
                    .setPositiveButton(com.tokopedia.core.R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PhoneVerificationFragmentPermissionsDispatcher
                                    .checkSmsPermissionWithCheck(PhoneVerificationFragment.this);

                        }
                    })
                    .setNegativeButton(com.tokopedia.core.R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestPermissionUtil.onPermissionDenied(getActivity(),
                                    Manifest.permission.READ_SMS);
                        }
                    })
                    .show();
        } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            PhoneVerificationFragmentPermissionsDispatcher
                    .checkSmsPermissionWithCheck(PhoneVerificationFragment.this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (smsReceiver != null)
            getActivity().unregisterReceiver(smsReceiver);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(
                SessionHandler.getPhoneNumber()));
        if (!cacheHandler.isExpired() && cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            inputOtpView.setVisibility(View.VISIBLE);
            changePhoneNumberButton.setVisibility(View.GONE);
            startTimer();
        }
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
        presenter = new PhoneVerificationPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_phone_verification;
    }

    @Override
    protected void initView(View view) {
        findView(view);

        KeyboardHandler.DropKeyboard(getActivity(), getView());

        Spannable spannable = new SpannableString(getString(com.tokopedia.core.R.string.action_send_otp_with_call_2));

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
                , getString(com.tokopedia.core.R.string.action_send_otp_with_call_2).indexOf("lewat")
                , getString(com.tokopedia.core.R.string.action_send_otp_with_call_2).length()
                , 0);

        requestOtpCallButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }


    @Override
    protected void setViewListener() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.verifyPhoneNumber();
            }
        });
        requestOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOtp();
            }
        });

        requestOtpCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.requestOtpWithCall();

            }
        });
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                                getActivity(),
                                phoneNumberEditText.getText().toString()),
                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
            }
        });

        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    verifyButton.setEnabled(true);
                    MethodChecker.setBackground(verifyButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button));
                    verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));

                } else {
                    verifyButton.setEnabled(false);
                    MethodChecker.setBackground(verifyButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.cards_grey));
                    verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onSkipVerification();
                else {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            }
        });

    }

    @Override
    protected void initialVar() {
        cacheHandler = new LocalCacheHandler(getActivity(), CACHE_PHONE_VERIF_TIMER);

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSuccessRequestOtp(String status) {
        finishProgressDialog();
        SnackbarManager.make(getActivity(), status, Snackbar.LENGTH_LONG).show();
        inputOtpView.setVisibility(View.VISIBLE);
        changePhoneNumberButton.setVisibility(View.GONE);
        setViewEnabled(true);
        startTimer();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString().replace("-", "");
    }

    @Override
    public void onErrorRequestOTP(String errorMessage) {
        setViewEnabled(true);
        finishProgressDialog();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    private void finishProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS);

        if (getActivity() != null)
            progressDialog.showDialog();
    }

    @Override
    public void onSuccessVerifyPhoneNumber() {
        finishProgressDialog();
        setViewEnabled(true);
        SessionHandler.setIsMSISDNVerified(true);
        SessionHandler.setPhoneNumber(phoneNumberEditText.getText().toString().replace("-", ""));
        if (listener != null)
            listener.onSuccessVerification();
        else {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_verify_phone_number));
    }

    @Override
    public void showErrorPhoneNumber(String errorMessage) {
        phoneNumberEditText.setError(errorMessage);

    }

    @Override
    public String getOTPCode() {
        return otpEditText.getText().toString();
    }

    @Override
    public void onErrorVerifyPhoneNumber(String errorMessage) {
        setViewEnabled(true);
        finishProgressDialog();
        if (errorMessage.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
        requestOtpButton.setEnabled(isEnabled);
        requestOtpCallButton.setEnabled(isEnabled);
        if (inputOtpView.getVisibility() == View.VISIBLE)
            verifyButton.setEnabled(isEnabled);
        skipButton.setEnabled(isEnabled);
    }

    private void startTimer() {
        if (cacheHandler.isExpired() || !cacheHandler.getBoolean(HAS_PHONE_VERIF_TIMER, false)) {
            cacheHandler.putBoolean(HAS_PHONE_VERIF_TIMER, true);
            cacheHandler.setExpire(DEFAULT_COUNTDOWN_TIMER_SECOND);
            cacheHandler.applyEditor();
        }

        countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000, COUNTDOWN_INTERVAL_SECOND) {
            public void onTick(long millisUntilFinished) {
                requestOtpButton.setVisibility(View.GONE);
                countdownText.setVisibility(View.VISIBLE);
                countdownText.setText(MethodChecker.fromHtml(
                        "Verifikasi akan dikirimkan.<br>Tunggu " + "<b>" + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))
                                + " detik</b> untuk mengirim ulang."));
                requestOtpCallButton.setVisibility(View.GONE);
            }

            public void onFinish() {
                enableOtpButton();

            }

        }.start();
        otpEditText.requestFocus();
    }

    private void enableOtpButton() {
        requestOtpButton.setVisibility(View.VISIBLE);
        countdownText.setVisibility(View.GONE);
        MethodChecker.setBackground(requestOtpButton,
                MethodChecker.getDrawable(getActivity(),
                        com.tokopedia.core.R.drawable.cards_ui));
        requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_600));
        requestOtpButton.setText(com.tokopedia.session.R.string.title_resend_otp_sms);
        requestOtpCallButton.setVisibility(View.VISIBLE);
        changePhoneNumberButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            phoneNumberEditText.setText(data.getStringExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        presenter.onDestroyView();
        cacheHandler = null;
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        processOTPSMS(otpCode);
    }


    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOTPSMS(String otpCode) {
        if (otpEditText != null)
            otpEditText.setText(otpCode);
        presenter.verifyPhoneNumber();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PhoneVerificationFragmentPermissionsDispatcher.onRequestPermissionsResult(
                PhoneVerificationFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_SMS)
    void showRationaleForReadSms(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_SMS);
    }

    @OnPermissionDenied(Manifest.permission.READ_SMS)
    void showDeniedForReadSms() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_SMS)
    void showNeverAskForReadSms() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_SMS);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void checkSmsPermission() {

    }

}
