package com.tokopedia.session.register.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.session.register.view.viewmodel.LoginRegisterPhoneNumberModel;

/**
 * @author by yfsx on 26/2/18.
 */

public interface RegisterPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void showConfirmationPhoneNumber(String phoneNumber);

        void doRegisterPhoneNumber();

        void dismissLoading();

        void showLoading();

        void showAlreadyRegisteredDialog(String phoneNumber);

        void showSuccessRegisterPhoneNumber(LoginRegisterPhoneNumberModel model);

        void showErrorRegisterPhoneNumber(String message);

        Context getContext();

    }

    interface Presenter extends CustomerPresenter<View> {

        void checkPhoneNumber(String phoneNumber);

        void registerPhoneNumber(String phoneNumber);

    }
}
