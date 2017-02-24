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
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.activity.ChangePhoneNumberActivity;
import com.tokopedia.otp.phoneverification.interactor.PhoneVerificationNetworkInteractorImpl;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenter;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenterImpl;
import com.tokopedia.session.R;
import com.tokopedia.session.R2;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 2/22/17.
 */

@RuntimePermissions
public class PhoneVerificationFragment extends BasePresenterFragment<PhoneVerificationPresenter>
        implements PhoneVerificationFragmentView, IncomingSmsReceiver.ReceiveSMSListener {

    private static final String CAN_REQUEST_OTP_IMMEDIATELY = "auto_request_otp";
    private static final String FORMAT = "%02d";

    @BindView(R2.id.verify_button)
    TextView verifyButton;

    @BindView(R2.id.skip_button)
    TextView skipButton;

    @BindView(R2.id.phone_number)
    TextView phoneNumberEditText;

    @BindView(R2.id.change_phone_number_button)
    TextView changePhoneNumberButton;

    @BindView(R2.id.send_otp)
    TextView requestOtpButton;

    @BindView(R2.id.send_otp_call)
    TextView requestOtpCallButton;

    @BindView(R2.id.input_otp_view)
    View inputOtpView;

    @BindView(R2.id.input_otp)
    EditText otpEditText;

    CountDownTimer countDownTimer;
    IncomingSmsReceiver smsReceiver;
    TkpdProgressDialog progressDialog;


    public static PhoneVerificationFragment createInstance() {
        return new PhoneVerificationFragment();
    }

    public PhoneVerificationFragment() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);
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
                    .setMessage(RequestPermissionUtil.getNeedPermissionMessage(Manifest.permission.READ_SMS)
                    )
                    .setPositiveButton(com.tokopedia.core.R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PhoneVerificationFragmentPermissionsDispatcher.checkSmsPermissionWithCheck(PhoneVerificationFragment.this);

                        }
                    })
                    .setNegativeButton(com.tokopedia.core.R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
                        }
                    })
                    .show();
        } else if (getActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
            PhoneVerificationFragmentPermissionsDispatcher.checkSmsPermissionWithCheck(PhoneVerificationFragment.this);
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
        phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(SessionHandler.getPhoneNumber()));

//        if (TrackingUtils.getGtmString(CAN_REQUEST_OTP_IMMEDIATELY).equals("true"))
//            presenter.requestOtp();
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

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        bundle.putString(AccountsService.AUTH_KEY,
                "Bearer " + sessionHandler.getAccessToken(getActivity()));


        presenter = new PhoneVerificationPresenterImpl(this,
                new CompositeSubscription(),
                new PhoneVerificationNetworkInteractorImpl(new AccountsService(bundle)));
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
        KeyboardHandler.DropKeyboard(getActivity(), getView());

        Spannable spannable = new SpannableString(getString(com.tokopedia.core.R.string.action_send_otp_with_call));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , getString(com.tokopedia.core.R.string.action_send_otp_with_call).indexOf("kirim")
                , getString(com.tokopedia.core.R.string.action_send_otp_with_call).length()
                , 0);

        requestOtpCallButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void setViewListener() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.verifyOtp();
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
                startActivityForResult(ChangePhoneNumberActivity.getChangePhoneNumberIntent(getActivity()),
                        ChangePhoneNumberActivity.ACTION_CHANGE_PHONE_NUMBER);
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
                getActivity().finish();
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
    public void onSuccessRequestOtp(String status) {
        finishProgressDialog();
        SnackbarManager.make(getActivity(), status, Snackbar.LENGTH_LONG).show();
        requestOtpCallButton.setVisibility(View.VISIBLE);
        inputOtpView.setVisibility(View.VISIBLE);
        startTimer();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString().replace("-", "");
    }

    @Override
    public void onErrorRequestOTP(String errorMessage) {
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

    private void startTimer() {

        countDownTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                MethodChecker.setBackground(requestOtpButton,
                        MethodChecker.getDrawable(getActivity(), R.drawable.btn_transparent_disable));
                requestOtpButton.setEnabled(false);
                requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                requestOtpButton.setText(getString(R.string.title_resend_otp_sms) + " ( " + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)) + " )");
            }

            public void onFinish() {
                enableOtpButton();

            }

        }.start();
        otpEditText.requestFocus();
    }

    private void enableOtpButton() {
        requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(),
                com.tokopedia.core.R.color.tkpd_green_onboarding));
        MethodChecker.setBackground(requestOtpButton,
                MethodChecker.getDrawable(getActivity(),
                        com.tokopedia.core.R.drawable.btn_share_transaparent));
        requestOtpButton.setText(com.tokopedia.session.R.string.title_resend_otp_sms);
        requestOtpButton.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChangePhoneNumberActivity.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            phoneNumberEditText.setText(data.getStringExtra(ChangePhoneNumberActivity.EXTRA_PHONE_NUMBER));
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
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        processOTPSMS(otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOTPSMS(String otpCode) {
        if (otpEditText != null)
            otpEditText.setText(otpCode);
        presenter.verifyOtp();
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
