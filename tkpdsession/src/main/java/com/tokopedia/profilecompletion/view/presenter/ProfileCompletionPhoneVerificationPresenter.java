package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.domain.interactor.ValidateVerifyPhoneNumberUseCase;
import com.tokopedia.profilecompletion.view.subscriber.ProfileCompletionVerifyPhoneNumberSubscriber;
import com.tokopedia.profilecompletion.view.subscriber.RequestOTPProfileCompletionSubscriber;
import com.tokopedia.session.R;

import javax.inject.Inject;

/**
 * Created by nisie on 2/22/17.
 */

public class ProfileCompletionPhoneVerificationPresenter
        extends BaseDaggerPresenter<ProfileCompletionPhoneVerificationContract.View>
        implements ProfileCompletionPhoneVerificationContract.Presenter {

    private final SessionHandler sessionHandler;
    private final RequestOtpUseCase requestOtpUseCase;
    private final ValidateVerifyPhoneNumberUseCase validateVerifyPhoneNumberUseCase;

    @Inject
    public ProfileCompletionPhoneVerificationPresenter(
            RequestOtpUseCase requestOtpUseCase,
            ValidateVerifyPhoneNumberUseCase validateVerifyPhoneNumberUseCase,
            SessionHandler sessionHandler) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.validateVerifyPhoneNumberUseCase = validateVerifyPhoneNumberUseCase;
        this.sessionHandler = sessionHandler;
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
            getView().showProgressDialog();
            getView().setViewEnabled(false);
            validateVerifyPhoneNumberUseCase.execute(ValidateVerifyPhoneNumberUseCase.getParam(
                    ValidateOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION,
                    otpCode,
                    phoneNumber,
                    sessionHandler.getLoginID()),
                    new ProfileCompletionVerifyPhoneNumberSubscriber(getView()));

        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (getView().getPhoneNumber().length() == 0) {
            getView().showErrorPhoneNumber(getView().getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOtp() {
        if (isValid()) {
            getView().setViewEnabled(false);
            getView().showProgressDialog();

            requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                    RequestOtpUseCase.MODE_SMS,
                    getView().getPhoneNumber(),
                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION
                    ), new RequestOTPProfileCompletionSubscriber(getView())
            );
        }
    }

    @Override
    public void requestOtpWithCall() {
        if (isValid()) {
            getView().setViewEnabled(false);
            getView().showProgressDialog();

            requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                    RequestOtpUseCase.MODE_CALL,
                    getView().getPhoneNumber(),
                    RequestOtpUseCase.OTP_TYPE_PHONE_NUMBER_VERIFICATION
            ), new RequestOTPProfileCompletionSubscriber(getView()));
        }
    }

}
