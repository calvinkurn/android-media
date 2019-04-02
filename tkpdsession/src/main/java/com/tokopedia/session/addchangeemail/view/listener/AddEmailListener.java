package com.tokopedia.session.addchangeemail.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 09/03/18.
 */

public interface AddEmailListener {
    interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void onErrorCheckEmail(String error);

        void onSuccessCheckEmail();

        void dismissLoading();

        void showLoading();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initView();

        void submitEmail(String email);
    }
}
