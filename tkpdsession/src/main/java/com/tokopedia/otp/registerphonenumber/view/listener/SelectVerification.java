package com.tokopedia.otp.registerphonenumber.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 5/3/18.
 */

public interface SelectVerification {

    interface View extends CustomerView {
        void onMethodSelected(int type);
    }

    interface Presenter extends CustomerPresenter<SelectVerification.View> {

    }
}
