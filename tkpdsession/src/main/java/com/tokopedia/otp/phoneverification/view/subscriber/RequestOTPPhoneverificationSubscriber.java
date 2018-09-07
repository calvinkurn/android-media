package com.tokopedia.otp.phoneverification.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.phoneverification.view.listener.PhoneVerification;
import com.tokopedia.session.R;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class RequestOTPPhoneverificationSubscriber extends Subscriber<RequestOtpViewModel> {
    private final PhoneVerification.View viewListener;

    public RequestOTPPhoneverificationSubscriber(PhoneVerification.View viewListener) {
        this.viewListener = viewListener;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorRequestOTP(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        if (requestOtpViewModel.isSuccess() && !TextUtils.isEmpty(requestOtpViewModel
                .getMessageStatus())) {
            viewListener.onSuccessRequestOtp(requestOtpViewModel.getMessageStatus());
        } else if (requestOtpViewModel.isSuccess()) {
            viewListener.onSuccessRequestOtp(MainApplication.getAppContext().getString(R.string
                    .success_request_otp));
        } else {
            viewListener.onErrorRequestOTP(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
