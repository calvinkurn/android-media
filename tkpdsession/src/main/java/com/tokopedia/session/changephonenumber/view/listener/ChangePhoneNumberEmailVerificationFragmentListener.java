package com.tokopedia.session.changephonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by milhamj on 03/01/18.
 */

public interface ChangePhoneNumberEmailVerificationFragmentListener {
    public interface View extends CustomerView {
        void showLoading();

        void dismissLoading();

        void dropKeyboard();

        void onSendEmailSuccess();

        void onSendEmailError(String message);

        void onValidateOtpSuccess();

        void onValidateOtpError(String message);

        Context getContext();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();

        void sendEmail();

        void validateOtp(String otpCode);
    }
}
