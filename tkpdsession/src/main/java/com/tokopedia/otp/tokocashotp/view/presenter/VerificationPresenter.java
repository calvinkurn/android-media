package com.tokopedia.otp.tokocashotp.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.otp.tokocashotp.domain.interactor.RequestOtpTokoCashUseCase;
import com.tokopedia.otp.tokocashotp.domain.interactor.VerifyOtpTokoCashUseCase;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.subscriber.RequestOtpTokoCashSubscriber;
import com.tokopedia.otp.tokocashotp.view.subscriber.VerifyOtpTokoCashSubscriber;
import com.tokopedia.otp.tokocashotp.view.viewlistener.Verification;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerificationViewModel;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpTokoCashUseCase requestTokoCashOTPUseCase;
    private final VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase;

    @Inject
    public VerificationPresenter(RequestOtpTokoCashUseCase requestTokoCashOTPUseCase,
                                 VerifyOtpTokoCashUseCase verifyTokoCashOTPUseCase) {
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
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpTokoCashUseCase.TYPE_SMS), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
                case VerificationActivity.TYPE_PHONE_CALL:
                    requestTokoCashOTPUseCase.execute(RequestOtpTokoCashUseCase.getParam(viewModel
                            .getPhoneNumber(), RequestOtpTokoCashUseCase.TYPE_PHONE), new
                            RequestOtpTokoCashSubscriber(getView()));
                    break;
            }
        }
    }

    @Override
    public void verifyOtp(String phoneNumber, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();
        verifyTokoCashOTPUseCase.execute(VerifyOtpTokoCashUseCase.getParam(phoneNumber, otpCode), new
                VerifyOtpTokoCashSubscriber(getView()));
    }
}
