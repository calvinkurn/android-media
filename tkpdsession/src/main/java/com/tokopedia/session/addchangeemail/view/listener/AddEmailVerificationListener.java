package com.tokopedia.session.addchangeemail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 09/03/18.
 */

public interface AddEmailVerificationListener {
    interface View extends CustomerView {

        void enableNextButton();

        void disableNextButton();

        void onErrorRequest(String error);

        void onSuccessRequest();

        void onErrorVerify(String error);

        void onSuccessVerify();

        void dismissLoading();

        void showLoading();

        boolean isCountdownFinished();

        void dropKeyboard();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void sendRequest(String email);

        void sendVerify(String email, String uniqueCode);
    }
}
