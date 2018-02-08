package com.tokopedia.session.login.loginphonenumber.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 11/23/17.
 */

public interface LoginPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void goToNoTokocashAccountPage();

        void dismissLoading();

        void showLoading();

    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithPhoneNumber(String text);
    }
}
