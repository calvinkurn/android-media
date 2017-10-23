package com.tokopedia.otp.securityquestion;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.otp.securityquestion.domain.model.securityquestion.QuestionDomain;
import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 10/18/17.
 */

public interface SecurityQuestion {
    interface View extends CustomerView {
        void resetError();

        void showInvalidOtp();

        void onErrorVerifyTrueCaller(String errorMessage);

        void onErrorGetQuestion(String errorMessage);

        void onSuccessGetQuestionPhone(QuestionDomain questionDomain);

        void onSuccessGetQuestionEmail(QuestionDomain questionDomain);

        void showTrueCaller();

        void removeTrueCaller();

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorRequestOTP(String errorMessage);

        void onSuccessRequestOTP(String messageStatus);

        void onSuccessValidateOtp();

        void onErrorValidateOtp(String errorMessage);

        void onGoToPhoneVerification();

        void disableOtpButton();
    }

    interface Presenter extends CustomerPresenter<View> {
        void validateOTP(String otp);

        void requestOTPWithPhoneCall();

        void processTrueCaller(Intent data);

        void getQuestionForm(SecurityDomain securityDomain);

        void checkTrueCaller(Context context);

        void requestOTPWithSMS();

        void requestOTPWithEmail(String email);

    }
}
