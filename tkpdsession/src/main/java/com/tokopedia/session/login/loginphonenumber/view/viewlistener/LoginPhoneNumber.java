package com.tokopedia.session.login.loginphonenumber.view.viewlistener;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

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

        void onForbidden();

    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithPhoneNumber(String text);
    }
}
