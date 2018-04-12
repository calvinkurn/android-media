package com.tokopedia.otp.registerphonenumber.view.subscriber;

import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.registerphonenumber.view.listener.Verification;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.RequestOtpViewModel;

import rx.Subscriber;

/**
 * @author by yfsx on 5/3/18.
 */

public class RequestOtpSubscriber extends Subscriber<RequestOtpViewModel> {

    private final Verification.View view;

    public RequestOtpSubscriber(Verification.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {


    }

    @Override
    public void onError(Throwable e) {
        view.dismissLoadingProgress();
        view.onErrorGetOTP(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP(requestOtpViewModel.getMessageStatus());

    }
}
