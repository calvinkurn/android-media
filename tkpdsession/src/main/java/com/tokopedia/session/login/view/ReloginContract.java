package com.tokopedia.session.login.view;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 5/26/17.
 */

public interface ReloginContract {

    interface View extends CustomerView {
        void onErrorRelogin(String errorMessage);

        void onSuccessRelogin();
    }

    interface Presenter extends CustomerPresenter<View> {
        void makeLogin();
    }
}
