package com.tokopedia.otp.securityquestion.view.subscriber;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.entity.otp.RequestOtpData;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.data.viewmodel.RequestOtpViewModel;
import com.tokopedia.otp.securityquestion.SecurityQuestion;
import com.tokopedia.session.R;

import rx.Subscriber;

/**
 * @author by nisie on 10/21/17.
 */

public class RequestOTPSubscriber extends Subscriber<RequestOtpViewModel> {

    private final SecurityQuestion.View viewListener;

    public RequestOTPSubscriber(SecurityQuestion.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissLoadingProgress();
        viewListener.onErrorRequestOTP(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(RequestOtpViewModel requestOtpViewModel) {
        viewListener.dismissLoadingProgress();
        if (requestOtpViewModel.isSuccess() && !TextUtils.isEmpty(requestOtpViewModel
                .getMessageStatus()))
            viewListener.onSuccessRequestOTP(requestOtpViewModel.getMessageStatus());
        else if (requestOtpViewModel.isSuccess())
            viewListener.onSuccessRequestOTP(MainApplication.getAppContext().getString(R.string
                    .success_request_otp));
        else
            viewListener.onErrorRequestOTP(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
    }
}
