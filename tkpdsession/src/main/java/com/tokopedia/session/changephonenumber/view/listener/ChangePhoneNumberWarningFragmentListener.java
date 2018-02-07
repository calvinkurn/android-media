package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

/**
 * Created by milhamj on 18/12/17.
 */

public interface ChangePhoneNumberWarningFragmentListener {
    public interface View extends CustomerView {
        void showLoading();

        void dismissLoading();

        void onGetWarningSuccess(WarningViewModel warningViewModel);

        void onGetWarningError(String message);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();

        void getWarning();
    }
}
