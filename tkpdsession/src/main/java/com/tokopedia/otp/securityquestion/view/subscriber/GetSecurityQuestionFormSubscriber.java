package com.tokopedia.otp.securityquestion.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.securityquestion.view.listener.SecurityQuestion;
import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;

import rx.Subscriber;

/**
 * @author by nisie on 10/19/17.
 */

public class GetSecurityQuestionFormSubscriber extends Subscriber<QuestionViewModel> {

    private static final String OTP_PHONE_NUMBER = "1";
    private static final String OTP_EMAIL = "2";
    private final SecurityQuestion.View viewListener;

    public GetSecurityQuestionFormSubscriber(SecurityQuestion.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.dismissLoadingFull();
        viewListener.onErrorGetQuestion(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(QuestionViewModel questionViewModel) {
        viewListener.dismissLoadingFull();
        if (questionViewModel.getQuestion().equals(OTP_PHONE_NUMBER)) {
            viewListener.onSuccessGetQuestionPhone(questionViewModel);
        } else if (questionViewModel.getQuestion().equals(OTP_EMAIL)) {
            viewListener.onSuccessGetQuestionEmail(questionViewModel);
        } else {
            viewListener.onErrorGetQuestion(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
