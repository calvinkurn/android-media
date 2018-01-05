package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 03/01/18.
 */

public interface ChangePhoneNumberEmailVerificationFragmentListener {
    public interface View extends CustomerView {
        void showLoading();

        void dismissLoading();

        void dropKeyboard();

        void onSendEmailSuccess(Boolean isSuccess);

        void onSendEmailError(String message);

        void onValidateOtpSuccess(Boolean isSuccess);

        void onValidateOtpError(String message);
    }

    public interface Presenter extends CustomerPresenter<ChangePhoneNumberEmailVerificationFragmentListener.View> {
        void initView();

        void sendEmail();

        void validateOtp(String otpCode);
    }
}
