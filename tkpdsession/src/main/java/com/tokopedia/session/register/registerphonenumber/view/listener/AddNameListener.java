package com.tokopedia.session.register.registerphonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.session.register.registerphonenumber.view.viewmodel.LoginRegisterPhoneNumberModel;

/**
 * @author by yfsx on 22/03/18.
 */

public interface AddNameListener {
    interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void onErrorRegister(String error);

        void onSuccessRegister(LoginRegisterPhoneNumberModel model);

        void dismissLoading();

        void showLoading();

        String getPhoneNumber();

        void showValidationError(String error);

        void hideValidationError();

        Context getContext();

        void onSuccessAddName();
    }

    interface Presenter extends CustomerPresenter<AddNameListener.View> {
        void initView();

        void registerPhoneNumberAndName(String name, String uuid, String phoneNumber);

        void addName(String name);
    }
}
