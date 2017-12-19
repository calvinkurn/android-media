package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 18/12/17.
 */

public interface ChangePhoneNumberWarningFragmentListener {
    public interface View extends CustomerView {

    }

    public interface Presenter {
        void initView();
    }
}
