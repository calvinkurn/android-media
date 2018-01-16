package com.tokopedia.otp.securityquestion.view.listener;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;
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

        void onSuccessGetQuestionPhone(QuestionViewModel questionViewModel);

        void onSuccessGetQuestionEmail(QuestionViewModel questionViewModel);

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

        void onSuccessGetTrueCallerData();

        String getString(int resId);

        void showLoadingFull();

        void dismissLoadingFull();
    }

    interface Presenter extends CustomerPresenter<View> {
        void validateOTP(String otp);

        void requestOTPWithPhoneCall(String phone);

        void processTrueCaller(Intent data);

        void getQuestionForm(SecurityDomain securityDomain);

        void checkTrueCaller(Context context);

        void requestOTPWithSMS(String phone);

        void requestOTPWithEmail(String email);
    }
}
