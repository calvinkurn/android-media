package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by milhamj on 03/01/18.
 */

public interface ChangePhoneNumberEmailVerificationActivityListener {
    interface View extends CustomerView {
        void inflateFragment();
    }
}
