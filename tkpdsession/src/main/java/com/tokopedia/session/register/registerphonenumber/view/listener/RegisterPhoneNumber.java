package com.tokopedia.session.register.registerphonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;

/**
 * @author by yfsx on 26/2/18.
 */

public interface RegisterPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void goToLoginPhoneNumber();

        void showConfirmationPhoneNumber(String phoneNumber);

        void doRegisterPhoneNumber();

        void dismissLoading();

        void showLoading();

        void showAlreadyRegisteredDialog(String phoneNumber);

        void showSuccessRegisterPhoneNumber(LoginRegisterPhoneNumberModel model);

        void showErrorRegisterPhoneNumber(String message);

        void dismissFocus();

        Context getContext();

    }

    interface Presenter extends CustomerPresenter<View> {

        void checkPhoneNumber(String phoneNumber);

        void registerPhoneNumber(String phoneNumber);

    }
}
