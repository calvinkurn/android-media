package com.tokopedia.session.register.view.viewlistener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 26/2/18.
 */

public interface RegisterPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void goToVerifyAccountPage(String phoneNumber);

        void goToNoTokocashAccountPage();

        void dismissLoading();

        void showLoading();

        void showAlreadyRegisteredDialog();

    }

    interface Presenter extends CustomerPresenter<View> {

        void registerWithPhoneNumber(String phoneNumber);
    }
}
