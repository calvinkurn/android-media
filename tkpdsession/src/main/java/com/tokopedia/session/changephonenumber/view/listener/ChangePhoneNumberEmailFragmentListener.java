package com.tokopedia.session.changephonenumber.view.listener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by milhamj on 20/12/17.
 */

public interface ChangePhoneNumberEmailFragmentListener {
    public interface View extends CustomerView {
        void showLoading();

        void dismissLoading();

        void onSendEmailSuccess(Boolean isSuccess);

        void onSendEmailError(String message);

        void onSendEmailFailed();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();

        void sendEmail();
    }
}
