package com.tokopedia.otp.tokocashotp.view.viewlistener;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * @author by nisie on 11/30/17.
 */

public interface SelectVerification {

    interface View extends CustomerView {
        void onMethodSelected(int type);
    }

    interface Presenter extends CustomerPresenter<SelectVerification.View> {

    }
}
