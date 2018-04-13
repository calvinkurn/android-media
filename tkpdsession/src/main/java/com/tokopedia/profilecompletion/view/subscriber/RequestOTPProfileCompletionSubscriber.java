package com.tokopedia.profilecompletion.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPhoneVerificationContract;
import com.tokopedia.session.R;

import rx.Subscriber;

/**
 * @author by nisie on 10/24/17.
 */

public class RequestOTPProfileCompletionSubscriber extends Subscriber<RequestOtpViewModel> {
    private final ProfileCompletionPhoneVerificationContract.View view;

    public RequestOTPProfileCompletionSubscriber(ProfileCompletionPhoneVerificationContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        view.onErrorRequestOTP(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        if (requestOtpViewModel.isSuccess() && !TextUtils.isEmpty(requestOtpViewModel
                .getMessageStatus())) {
            view.onSuccessRequestOtp(requestOtpViewModel.getMessageStatus());
        } else if (requestOtpViewModel.isSuccess()) {
            view.onSuccessRequestOtp(MainApplication.getAppContext().getString(R.string
                    .success_request_otp));
        } else {
            view.onErrorRequestOTP(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
