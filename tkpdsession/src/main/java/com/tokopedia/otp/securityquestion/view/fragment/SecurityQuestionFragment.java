package com.tokopedia.otp.securityquestion.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.msisdn.IncomingSmsReceiver;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.otp.securityquestion.SecurityQuestion;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.otp.securityquestion.view.activity.ChangePhoneNumberRequestActivity;
import com.tokopedia.otp.securityquestion.view.activity.SecurityQuestionActivity;
import com.tokopedia.otp.securityquestion.view.presenter.SecurityQuestionPresenter;
import com.tokopedia.otp.securityquestion.view.viewmodel.SecurityQuestionViewModel;
import com.tokopedia.session.R;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.di.DaggerSessionComponent;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author by nisie on 10/18/17.
 */

@RuntimePermissions
public class SecurityQuestionFragment extends BaseDaggerFragment
        implements SecurityQuestion.View, IncomingSmsReceiver.ReceiveSMSListener {

    private static final String CAN_REQUEST_OTP_IMMEDIATELY = "auto_request_otp";
    private static final String TRUE = "true";

    private static final String FORMAT = "%02d:%02d";

    private static final int REQUEST_TRUE_CALLER = 100;
    private static final int REQUEST_CHANGE_PHONE_NUMBER = 101;
    private static final int REQUEST_VERIFY_PHONE_NUMBER = 102;

    private static final String ARGS_DATA = "ARGS_DATA";

    private EditText vInputOtp;
    private TextView titleOTP;
    private TextView titleSecurity;
    private View vOtp;
    private View vError;
    private TextView vSendOtp;
    private TextView vSendOtpCall;
    private TextView vSaveBut;
    private TextView changeNumber;
    private TextView verifyTrueCaller;

    private CountDownTimer countDownTimer;
    private IncomingSmsReceiver smsReceiver;
    private TkpdProgressDialog Progress;

    boolean isRunningTimer;

    @Inject
    SecurityQuestionPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    SecurityQuestionViewModel securityQuestionViewModel;

    public static Fragment createInstance(Bundle bundle) {
        SecurityQuestionFragment fragment = new SecurityQuestionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public SecurityQuestionFragment() {
        this.smsReceiver = new IncomingSmsReceiver();
        this.smsReceiver.setListener(this);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_SECURITY_QUESTION;
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
            securityQuestionViewModel = savedInstanceState.getParcelable(ARGS_DATA);
        } else if (getArguments() != null) {
            securityQuestionViewModel = new SecurityQuestionViewModel(
                    (SecurityDomain) getArguments().getParcelable(SecurityQuestionActivity.ARGS_QUESTION),
                    getArguments().getString(SecurityQuestionActivity.ARGS_NAME),
                    getArguments().getString(SecurityQuestionActivity.ARGS_EMAIL));
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_question, parent, false);

        vInputOtp = (EditText) view.findViewById(R.id.input_otp);
        titleOTP = (TextView) view.findViewById(R.id.title_otp);
        titleSecurity = (TextView) view.findViewById(R.id.title_security);
        vOtp = view.findViewById(R.id.view_otp);
        vError = view.findViewById(R.id.view_error);
        vSendOtp = (TextView) view.findViewById(R.id.send_otp);
        vSendOtpCall = (TextView) view.findViewById(R.id.send_otp_call);
        vSaveBut = (TextView) view.findViewById(R.id.save_but);
        changeNumber = (TextView) view.findViewById(R.id.title_change_number);
        verifyTrueCaller = (TextView) view.findViewById(R.id.verify_button);
        prepareView();
        setViewListener();
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getQuestionForm(securityQuestionViewModel.getSecurityDomain());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (smsReceiver != null && getActivity() != null)
            smsReceiver.registerSmsReceiver(getActivity());
        ScreenTracking.screen(AppScreen.SCREEN_OTP_SQ);
    }

    @Override
    public void onPause() {
        if (smsReceiver != null && getActivity() != null)
            getActivity().unregisterReceiver(smsReceiver);
        super.onPause();
    }

    private void setViewListener() {
        vSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.validateOTP(vInputOtp.getText().toString());

            }
        });

        vSendOtpCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.requestOTPWithPhoneCall();

            }
        });

        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChangePhoneNumberPage();
            }
        });
        verifyTrueCaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrueCaller();
                UnifyTracking.eventClickTruecaller();
            }
        });

    }

    private void openTrueCaller() {

        if (MainApplication.getAppContext() instanceof SessionRouter) {
            Intent intent = ((SessionRouter) (MainApplication.getAppContext()))
                    .getTrueCallerIntent(getActivity());
            startActivityForResult(intent, REQUEST_TRUE_CALLER);
        }
    }

    private void goToChangePhoneNumberPage() {
        Intent intent = ChangePhoneNumberRequestActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_CHANGE_PHONE_NUMBER);
    }

    private void prepareView() {
        vOtp.setVisibility(View.VISIBLE);

        String title;
        if (getArguments() != null) {
            title = getString(R.string.hi) + " "
                    + getArguments().getString(SecurityQuestionActivity.ARGS_NAME, "")
                    + ",";
            titleOTP.setText(title);
        }

        Spannable spannable2 = new SpannableString(getString(R.string.content_change_number));

        spannable2.setSpan(new ClickableSpan() {
                               @Override
                               public void onClick(View view) {

                               }

                               @Override
                               public void updateDrawState(TextPaint ds) {
                                   ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                               }
                           }
                , getString(R.string.content_change_number).indexOf("klik disini")
                , getString(R.string.content_change_number).length()
                , 0);

        changeNumber.setText(spannable2, TextView.BufferType.SPANNABLE);

        Spannable spannable = new SpannableString(getString(R.string.action_send_otp_with_call));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , getString(R.string.action_send_otp_with_call).indexOf("lewat")
                , getString(R.string.action_send_otp_with_call).length()
                , 0);


        vSendOtpCall.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void resetError() {
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        vError.setVisibility(View.GONE);

    }

    @Override
    public void showInvalidOtp() {
        vError.setVisibility(View.VISIBLE);
        vInputOtp.requestFocus();
    }

    @Override
    public void onErrorVerifyTrueCaller(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onErrorGetQuestion(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getQuestionForm(securityQuestionViewModel.getSecurityDomain());
            }
        });
    }

    @Override
    public void onSuccessGetQuestionPhone(QuestionDomain questionDomain) {
        vSendOtp.setText(R.string.title_otp_phone);
        vInputOtp.setEnabled(true);
        String phone = sessionHandler.getTempPhoneNumber(getActivity());
        phone = phone.substring(phone.length() - 4);
        String contentSecurity = String.format(getResources().getString(
                R.string.content_security_question_phone) + " <b>XXXX-XXXX- %s </b>", phone);
        titleSecurity.setText(MethodChecker.fromHtml(contentSecurity));
        changeNumber.setVisibility(View.VISIBLE);
        vSendOtpCall.setVisibility(View.VISIBLE);
        presenter.checkTrueCaller(getActivity());
        if (isAutoRequestOTP()) {
            presenter.requestOTPWithSMS();
        }
        vSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOTPWithSMS();
            }
        });
    }

    private boolean isAutoRequestOTP() {
        return TrackingUtils.getGtmString(CAN_REQUEST_OTP_IMMEDIATELY).equals(TRUE) &&
                !verifyTrueCaller.isShown();
    }

    @Override
    public void onSuccessGetQuestionEmail(QuestionDomain questionDomain) {
        vSendOtp.setText(R.string.title_otp_email);
        vInputOtp.setEnabled(true);
        titleSecurity.setText(getResources().getString(R.string.content_security_question_email));
        changeNumber.setVisibility(View.GONE);
        vSendOtpCall.setVisibility(View.GONE);
        verifyTrueCaller.setVisibility(View.GONE);
        vSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOTPWithEmail(securityQuestionViewModel.getEmail());
            }
        });
        if (isAutoRequestOTP()) {
            presenter.requestOTPWithEmail(securityQuestionViewModel.getEmail());
        }
    }

    @Override
    public void showTrueCaller() {
        Drawable img = MethodChecker.getDrawable(getActivity(), R.drawable.truecaller);
        img.setBounds(0, 0, 75, 75);
        verifyTrueCaller.setCompoundDrawables(img, null, null, null);

        verifyTrueCaller.setVisibility(View.VISIBLE);
        UnifyTracking.eventTruecallerImpression();
    }

    @Override
    public void removeTrueCaller() {
        verifyTrueCaller.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void dismissLoadingProgress() {

    }

    @Override
    public void onErrorRequestOTP(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRequestOTP(String messageStatus) {
        NetworkErrorHelper.showSnackbar(getActivity(), messageStatus);
    }

    @Override
    public void onSuccessValidateOtp() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onErrorValidateOtp(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onGoToPhoneVerification() {
        Intent intent = PhoneVerificationActivationActivity.getCallingIntent(getActivity());
        startActivityForResult(intent, REQUEST_VERIFY_PHONE_NUMBER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TRUE_CALLER && resultCode == Activity.RESULT_OK) {
            presenter.processTrueCaller(data);
        } else if (requestCode == REQUEST_CHANGE_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }else if (requestCode == REQUEST_VERIFY_PHONE_NUMBER){
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onReceiveOTP(String otpCode) {
        SecurityQuestionFragmentPermissionsDispatcher.processOtpWithCheck(SecurityQuestionFragment.this,
                otpCode);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void processOtp(String otpCode) {
        if (vInputOtp != null)
            vInputOtp.setText(otpCode);
        presenter.validateOTP(otpCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SecurityQuestionFragmentPermissionsDispatcher.onRequestPermissionsResult(SecurityQuestionFragment.this,
                requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_SMS)
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_SMS);
    }

    @OnPermissionDenied(Manifest.permission.READ_SMS)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_SMS);
    }

    @OnNeverAskAgain(Manifest.permission.READ_SMS)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_SMS);
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void checkSmsPermission() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_DATA, securityQuestionViewModel);
    }
}
