package com.tokopedia.session.login.loginphonenumber.viewlistener;

import android.text.Editable;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 11/23/17.
 */

public interface LoginPhoneNumber {
    interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void goToVerifyAccountPage(String phoneNumber);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loginWithPhoneNumber(String text);
    }
}
