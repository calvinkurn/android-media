package com.tokopedia.session.register.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;

/**
 * @author by nisie on 10/13/17.
 */

public interface CreatePassword {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {

        void createPassword(CreatePasswordModel model);
    }
}
