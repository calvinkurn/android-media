package com.tokopedia.otp.tokocashotp.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;
import com.tokopedia.otp.tokocashotp.view.viewlistener.Verification;
import com.tokopedia.session.R;

import rx.Subscriber;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpTokoCashSubscriber extends Subscriber<RequestOtpTokoCashViewModel> {

    private final Verification.View view;

    public RequestOtpTokoCashSubscriber(Verification.View view) {
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
    public void onNext(RequestOtpTokoCashViewModel requestOtpTokoCashViewModel) {
        view.dismissLoadingProgress();
        view.onSuccessGetOTP();

    }
}
