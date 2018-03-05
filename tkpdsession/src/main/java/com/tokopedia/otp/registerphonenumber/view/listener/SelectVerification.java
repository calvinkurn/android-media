package com.tokopedia.otp.registerphonenumber.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by yfsx on 3/5/17.
 */

public interface SelectVerification {

    interface View extends CustomerView {
        void onMethodSelected(int type);
    }

    interface Presenter extends CustomerPresenter<SelectVerification.View> {

    }
}
