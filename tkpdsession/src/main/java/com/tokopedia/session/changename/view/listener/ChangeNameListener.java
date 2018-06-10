package com.tokopedia.session.changename.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 22/03/18.
 */

public interface ChangeNameListener {
    interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void onErrorSubmitName(String error);

        void onSuccessSubmitName();

        void dismissLoading();

        void showLoading();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<ChangeNameListener.View> {
        void initView();

        void submitName(String name);
    }
}
