package com.tokopedia.otp.registerphonenumber.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.otp.registerphonenumber.domain.usecase.RequestOtpUseCase;
import com.tokopedia.otp.registerphonenumber.domain.usecase.VerifyOtpUseCase;
import com.tokopedia.otp.registerphonenumber.view.activity.VerificationActivity;
import com.tokopedia.otp.registerphonenumber.view.listener.Verification;
import com.tokopedia.otp.registerphonenumber.view.subscriber.RequestOtpSubscriber;
import com.tokopedia.otp.registerphonenumber.view.subscriber.VerifyOtpSubscriber;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.VerificationViewModel;

import javax.inject.Inject;

/**
 * @author by yfsx on 5/3/18.
 */

public class VerificationPresenter extends BaseDaggerPresenter<Verification.View> implements
        Verification.Presenter {

    private final RequestOtpUseCase requestOtpUseCase;
    private final VerifyOtpUseCase verifyOtpUseCase;

    @Inject
    public VerificationPresenter(RequestOtpUseCase requestOtpUseCase,
                                 VerifyOtpUseCase verifyOtpUseCase) {
        this.requestOtpUseCase = requestOtpUseCase;
        this.verifyOtpUseCase = verifyOtpUseCase;
    }

    public void attachView(Verification.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        requestOtpUseCase.unsubscribe();
        verifyOtpUseCase.unsubscribe();
    }

    @Override
    public void requestOTP(VerificationViewModel viewModel) {
        if (!TextUtils.isEmpty(viewModel.getPhoneNumber()) && getView().isCountdownFinished()) {
            getView().showLoadingProgress();
            int type = viewModel.getType();
            switch (type) {
                case VerificationActivity.TYPE_SMS:
                    requestOtpUseCase.execute(RequestOtpUseCase.getParam(getView().getContext(),
                            RequestOtpUseCase.MODE_SMS,
                            viewModel.getPhoneNumber()), new
                            RequestOtpSubscriber(getView()));
                    break;
                case VerificationActivity.TYPE_PHONE_CALL:
                    requestOtpUseCase.execute(RequestOtpUseCase.getParam(getView().getContext(),
                            RequestOtpUseCase.MODE_CALL,
                            viewModel.getPhoneNumber()), new
                            RequestOtpSubscriber(getView()));
                    break;
            }
        }
    }

    @Override
    public void verifyOtp(String phoneNumber, String otpCode) {
        getView().dropKeyboard();
        getView().showLoadingProgress();
        verifyOtpUseCase.execute(VerifyOtpUseCase.getParam(phoneNumber, otpCode), new
                VerifyOtpSubscriber(getView()));
    }
}
