package com.tokopedia.otp.centralizedotp.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * @author by nisie on 11/30/17.
 */

public interface Verification {
    interface View extends CustomerView {
        void onSuccessGetOTP();
    }

    interface Presenter extends CustomerPresenter<Verification.View> {

        void requestOTP(String phoneNumber);
    }
}
