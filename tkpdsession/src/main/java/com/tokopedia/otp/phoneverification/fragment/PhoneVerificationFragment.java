package com.tokopedia.otp.phoneverification.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.otp.phoneverification.activity.ChangePhoneNumberActivity;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenter;
import com.tokopedia.otp.phoneverification.presenter.PhoneVerificationPresenterImpl;
import com.tokopedia.session.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationFragment extends BasePresenterFragment<PhoneVerificationPresenter>
        implements PhoneVerificationFragmentView {

    private static final String CAN_REQUEST_OTP_IMMEDIATELY = "auto_request_otp";
    private static final String FORMAT = "%02d:%02d";

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


    public static PhoneVerificationFragment createInstance() {
        return new PhoneVerificationFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

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
        KeyboardHandler.DropKeyboard(getActivity(), getView());
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

                }else{
                    verifyButton.setEnabled(false);
                    MethodChecker.setBackground(verifyButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.cards_grey));
                    verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                }
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
    public void onSuccessRequestOtp() {
        requestOtpCallButton.setVisibility(View.VISIBLE);
        inputOtpView.setVisibility(View.VISIBLE);
        startTimer();
    }

    private void startTimer() {

        countDownTimer = new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                MethodChecker.setBackground(requestOtpButton, getResources().getDrawable(com.tokopedia.core.R.drawable.btn_transparent_disable));
                requestOtpButton.setEnabled(false);
                requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                requestOtpButton.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                enableOtpButton();

            }

        }.start();
        otpEditText.requestFocus();
    }

    private void enableOtpButton() {
        requestOtpButton.setTextColor(MethodChecker.getColor(getActivity(), com.tokopedia.core.R.color.tkpd_green_onboarding));
        MethodChecker.setBackground(requestOtpButton,
                MethodChecker.getDrawable(getActivity(), com.tokopedia.core.R.drawable.btn_share_transaparent));
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
    }
}
