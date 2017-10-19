package com.tokopedia.otp.securityquestion.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.otp.securityquestion.SecurityQuestion;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.R;

import rx.Subscriber;

/**
 * @author by nisie on 10/19/17.
 */

public class GetSecurityQuestionFormSubscriber extends Subscriber<QuestionDomain> {

    private static final int OTP_PHONE_NUMBER = 1;
    private static final int OTP_EMAIL = 2;
    private final SecurityQuestion.View viewListener;

    public GetSecurityQuestionFormSubscriber(SecurityQuestion.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetQuestion(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(QuestionDomain questionDomain) {
        if (questionDomain.getQuestion() == OTP_PHONE_NUMBER) {
            viewListener.onSuccessGetQuestionPhone(questionDomain);
        } else if (questionDomain.getQuestion() == OTP_EMAIL) {
            viewListener.onSuccessGetQuestionEmail(questionDomain);
        } else {
            viewListener.onErrorGetQuestion(MainApplication.getAppContext().getString(R.string
                    .default_request_error_unknown) + " " + MainApplication.getAppContext().getString(R.string
                    .code_error) + " " + ErrorCode.UNKNOWN_SECURITY_QUESTION_TYPE);
        }
    }
}
