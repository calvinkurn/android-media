package com.tokopedia.session.changephonenumber.view.listener;

import android.app.Fragment;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 18/12/17.
 */

public interface ChangePhoneNumberWarningActivityListener {
    interface View extends CustomerView {
        void inflateFragment();
    }

}
