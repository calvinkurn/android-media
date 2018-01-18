package com.tokopedia.otp.tokocashotp.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

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
