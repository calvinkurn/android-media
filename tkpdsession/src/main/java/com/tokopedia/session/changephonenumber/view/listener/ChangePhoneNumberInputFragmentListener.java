package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 20/12/17.
 */

public interface ChangePhoneNumberInputFragmentListener {
    public interface View extends CustomerView {

    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();
    }
}
