package com.tokopedia.session.register.view.viewlistener;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

/**
 * @author by nisie on 10/13/17.
 */

public interface CreatePassword {

    interface View extends CustomerView {

        void resetError();

        void showErrorName(int resId);

        void showErrorPassword(int resId);

        void showErrorConfirmPassword(int resId);

        void showErrorPhoneNumber(int resId);

        void showErrorBday(int resId);

        void showErrorTOS();

        void onSuccessCreatePassword();

        void onErrorCreatePassword(String errorMessage);

        void onGoToPhoneVerification();
    }

    interface Presenter extends CustomerPresenter<View> {

        void createPassword(CreatePasswordViewModel model);
    }
}
