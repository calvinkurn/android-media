package com.tokopedia.otp.cotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.data.model.RequestOtpViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
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
        view.onSuccessGetOTP();
    }
}
