package com.tokopedia.otp.cotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.domain.interactor.VerifyOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.subscriber.RequestOtpSubscriber;
import com.tokopedia.otp.cotp.view.subscriber.VerifyOtpSubscriber;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpUseCase requestTokoCashOTPUseCase;
    private final VerifyOtpUseCase verifyTokoCashOTPUseCase;

    @Inject
    public VerificationPresenter(RequestOtpUseCase requestTokoCashOTPUseCase,
                                 VerifyOtpUseCase verifyTokoCashOTPUseCase) {
        this.requestTokoCashOTPUseCase = requestTokoCashOTPUseCase;
        this.verifyTokoCashOTPUseCase = verifyTokoCashOTPUseCase;
    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestTokoCashOTPUseCase.unsubscribe();
        verifyTokoCashOTPUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(VerificationViewModel viewModel) {
        if (!TextUtils.isEmpty(viewModel.getPhoneNumber()) && getView().isCountdownFinished()) {
            getView().showLoadingProgress();
            int type = viewModel.getType();
            switch (type) {
                case VerificationActivity.TYPE_SMS:
                    requestTokoCashOTPUseCase.execute(RequestOtpUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpUseCase.TYPE_SMS), new
                            RequestOtpSubscriber(getView()));
                    break;
                case VerificationActivity.TYPE_PHONE_CALL:
                    requestTokoCashOTPUseCase.execute(RequestOtpUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpUseCase.TYPE_PHONE), new
                            RequestOtpSubscriber(getView()));
                    break;
            }
        }
    }

    @Override
    public void verifyOtp(String phoneNumber, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();
        verifyTokoCashOTPUseCase.execute(VerifyOtpUseCase.getParam(phoneNumber, otpCode), new
                VerifyOtpSubscriber(getView()));
    }
}
