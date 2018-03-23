package com.tokopedia.session.addchangepassword.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 22/03/18.
 */

public interface AddPasswordListener {
    interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void onErrorSubmitPassword(String error);

        void onSuccessSubmitPassword();

        void dismissLoading();

        void showLoading();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<AddPasswordListener.View> {

        void checkPassword(String password);

        void initView();

        void submitPassword(String password);
    }
}
