package com.tokopedia.otp.cotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.subscriber.RequestOtpSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.ValidateOtpLoginSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.VerifyOtpSubscriber;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.domain.interactor.RequestOtpWithEmailUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpLoginUseCase;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpUseCase requestOtpUseCase;
    private final RequestOtpWithEmailUseCase requestOtpEmailUseCase;
    private final ValidateOtpLoginUseCase validateOtpLoginUseCase;
    private final ValidateOtpUseCase validateOtpUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public VerificationPresenter(SessionHandler sessionHandler,
                                 RequestOtpUseCase requestOtpUseCase,
                                 RequestOtpWithEmailUseCase requestOtpEmailUseCase,
                                 ValidateOtpLoginUseCase validateOtpLoginUseCase,
                                 ValidateOtpUseCase validateOtpUseCase) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.requestOtpEmailUseCase = requestOtpEmailUseCase;
        this.validateOtpLoginUseCase = validateOtpLoginUseCase;
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
        validateOtpLoginUseCase.unsubscribe();
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
            case RequestOtpUseCase.MODE_EMAIL:
                if (!TextUtils.isEmpty(passModel.getEmail())) {
                    requestOtpEmailUseCase.execute(RequestOtpWithEmailUseCase.getParam(
                            passModel.getEmail(),
                            passModel.getOtpType(),
                            sessionHandler.getLoginID()
                    ), new RequestOtpSubscriber(getView()));
                }
            default:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamAfterLogin(
                            viewModel.getType(),
                            passModel.getPhoneNumber(),
                            passModel.getOtpType()
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
        }
    }

    private void handleOtpSecurityQuestion(VerificationViewModel viewModel, VerificationPassModel passModel) {
        switch (viewModel.getType()) {
            case RequestOtpUseCase.MODE_EMAIL:
                if (!TextUtils.isEmpty(passModel.getEmail())) {
                    requestOtpEmailUseCase.execute(RequestOtpWithEmailUseCase.getParam(
                            passModel.getEmail(),
                            passModel.getOtpType(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
            default:
                if (!TextUtils.isEmpty(passModel.getPhoneNumber())) {
                    requestOtpUseCase.execute(RequestOtpUseCase.getParamBeforeLogin(
                            viewModel.getType(),
                            passModel.getPhoneNumber(),
                            passModel.getOtpType(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ), new RequestOtpSubscriber(getView()));
                }
                break;
        }
    }

    @Override
    public void verifyOtp(VerificationPassModel passModel, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();

        int otpType = passModel.getOtpType();
        switch (otpType) {
            case RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION:
                validateOtpLoginUseCase.execute(ValidateOtpLoginUseCase.getParam(
                        passModel.getOtpType(),
                        otpCode,
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                ), new ValidateOtpLoginSubscriber(getView()));
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
