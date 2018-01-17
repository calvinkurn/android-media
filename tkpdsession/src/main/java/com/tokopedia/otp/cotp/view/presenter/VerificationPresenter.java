package com.tokopedia.otp.cotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.subscriber.RequestOtpSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.VerifyOtpSubscriber;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.RequestOtpWithEmailUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpUseCase requestOtpUseCase;
    private final RequestOtpWithEmailUseCase requestOtpEmailUseCase;
    private final ValidateOtpUseCase validateOtpUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public VerificationPresenter(SessionHandler sessionHandler,
                                 RequestOtpUseCase requestOtpUseCase,
                                 RequestOtpWithEmailUseCase requestOtpEmailUseCase,
                                 ValidateOtpUseCase validateOtpUseCase) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.requestOtpEmailUseCase = requestOtpEmailUseCase;
        this.validateOtpUseCase = validateOtpUseCase;
        this.sessionHandler = sessionHandler;
    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestOtpEmailUseCase.unsubscribe();
        requestOtpUseCase.unsubscribe();
        validateOtpUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(VerificationViewModel viewModel, VerificationPassModel passModel) {
        if (getView().isCountdownFinished()) {
            getView().showLoadingProgress();
            int otpType = passModel.getOtpType();
            switch (otpType) {
                case RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION:
                    handleOtpSecurityQuestion(viewModel, passModel);
                    break;
                default:
                    handleOtp(viewModel, passModel);
                    break;

            }
        }
    }

    private void handleOtp(VerificationViewModel viewModel, VerificationPassModel passModel) {
        switch (viewModel.getType()) {
            case VerificationActivity.TYPE_SMS:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                            RequestOtpUseCase.MODE_SMS,
                            passModel.getPhoneNumber(),
                            passModel.getOtpType()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            case VerificationActivity.TYPE_PHONE_CALL:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                            RequestOtpUseCase.MODE_CALL,
                            passModel.getPhoneNumber(),
                            passModel.getOtpType()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            case VerificationActivity.TYPE_EMAIL:
                if (!TextUtils.isEmpty(passModel.getEmail())) {
                    requestOtpEmailUseCase.execute(RequestOtpWithEmailUseCase.getParam(
                            passModel.getEmail(),
                            passModel.getOtpType(),
                            sessionHandler.getLoginID()
                    ), new RequestOtpSubscriber(getView()));
                }
            default:
                throw new RuntimeException("Verification Type not supported");
        }
    }

    private void handleOtpSecurityQuestion(VerificationViewModel viewModel, VerificationPassModel passModel) {
        switch (viewModel.getType()) {
            case VerificationActivity.TYPE_SMS:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamBeforeLogin(
                            RequestOtpUseCase.MODE_SMS,
                            passModel.getPhoneNumber(),
                            passModel.getOtpType(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            case VerificationActivity.TYPE_PHONE_CALL:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamBeforeLogin(
                            RequestOtpUseCase.MODE_CALL,
                            passModel.getPhoneNumber(),
                            passModel.getOtpType(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            case VerificationActivity.TYPE_EMAIL:
                if (!TextUtils.isEmpty(passModel.getEmail())) {
                    requestOtpEmailUseCase.execute(RequestOtpWithEmailUseCase.getParam(
                            passModel.getEmail(),
                            passModel.getOtpType(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            default:
                throw new RuntimeException("Verification Type not supported");
        }
    }

    @Override
    public void verifyOtp(VerificationPassModel passModel, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();

        int otpType = passModel.getOtpType();
        switch (otpType) {
            case RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION:
                validateOtpUseCase.execute(ValidateOtpUseCase.getParam(
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext()),
                        passModel.getOtpType(),
                        otpCode
                ), new VerifyOtpSubscriber(getView()));
                break;
            default:
                validateOtpUseCase.execute(ValidateOtpUseCase.getParam(
                        sessionHandler.getLoginID(),
                        passModel.getOtpType(),
                        otpCode
                ), new VerifyOtpSubscriber(getView()));
                break;
        }
    }
}
