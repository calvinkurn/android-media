package com.tokopedia.session.login.loginphonenumber.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccount {
    interface View extends CustomerView {

        void onSelectedTokocashAccount(AccountTokocash accountTokocash);

        void onSuccessLogin();

    }

    interface Presenter extends CustomerPresenter<ChooseTokocashAccount.View> {

        void loginWithTokocash(AccountTokocash accountTokocash);
    }
}
