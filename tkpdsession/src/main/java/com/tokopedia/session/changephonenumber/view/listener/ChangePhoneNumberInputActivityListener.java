package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

/**
 * Created by milhamj on 20/12/17.
 */

public interface ChangePhoneNumberInputActivityListener {
    interface View extends CustomerView {
        void inflateFragment();
    }

}
