package com.tokopedia.otp.phoneverification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.domain.interactor.ValidateVerifyPhoneNumberUseCase;
import com.tokopedia.otp.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.otp.phoneverification.view.subscriber.RequestOTPPhoneverificationSubscriber;
import com.tokopedia.otp.phoneverification.view.subscriber.VerifyPhoneNumberSubscriber;
import com.tokopedia.session.R;

import javax.inject.Inject;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationPresenter extends BaseDaggerPresenter<PhoneVerification.View>
        implements PhoneVerification.Presenter {

    private final RequestOtpUseCase requestOtpUseCase;
    private final ValidateVerifyPhoneNumberUseCase validateVerifyPhoneNumberUseCase;
    private final SessionHandler sessionHandler;
    private PhoneVerification.View viewListener;

    @Inject
    public PhoneVerificationPresenter(RequestOtpUseCase requestOtpUseCase,
                                      ValidateVerifyPhoneNumberUseCase
                                              validateVerifyPhoneNumberUseCase,
                                      SessionHandler sessionHandler) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.validateVerifyPhoneNumberUseCase = validateVerifyPhoneNumberUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(PhoneVerification.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        requestOtpUseCase.unsubscribe();
        validateVerifyPhoneNumberUseCase.unsubscribe();
    }

    @Override
    public void verifyPhoneNumber(String otpCode, String phoneNumber) {
        if (isValid()) {
            viewListener.showProgressDialog();
            viewListener.setViewEnabled(false);

            validateVerifyPhoneNumberUseCase.execute(ValidateVerifyPhoneNumberUseCase.getParam(
                    ValidateOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
                    otpCode,
                    phoneNumber,
                    sessionHandler.getLoginID()),
                    new VerifyPhoneNumberSubscriber(viewListener));

        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (viewListener.getPhoneNumber().length() == 0) {
            viewListener.showErrorPhoneNumber(viewListener.getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOtp() {
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

            requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                    RequestOtpUseCase.MODE_SMS,
                    viewListener.getPhoneNumber(),
                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION
                    ), new RequestOTPPhoneverificationSubscriber(viewListener)
            );
        }
    }

    @Override
    public void requestOtpWithCall() {
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

            requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                    RequestOtpUseCase.MODE_CALL,
                    viewListener.getPhoneNumber(),
                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION
            ), new RequestOTPPhoneverificationSubscriber(viewListener));
        }
    }
}
