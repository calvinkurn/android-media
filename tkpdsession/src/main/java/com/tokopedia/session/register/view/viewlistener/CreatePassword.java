package com.tokopedia.session.register.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;

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
    }

    interface Presenter extends CustomerPresenter<View> {

        void createPassword(CreatePasswordModel model);
    }
}
