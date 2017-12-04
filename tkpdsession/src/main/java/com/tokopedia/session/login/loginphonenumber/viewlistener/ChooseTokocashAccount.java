package com.tokopedia.session.login.loginphonenumber.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.login.loginphonenumber.viewmodel.AccountTokocash;

import java.util.ArrayList;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccount {
    interface View extends CustomerView {

        void onSelectedTokocashAccount(AccountTokocash accountTokocash);

        void onSuccessGetTokocashAccounts(ArrayList<AccountTokocash> accountTokocashes);

        void onSuccessLogin();

    }

    interface Presenter extends CustomerPresenter<ChooseTokocashAccount.View> {

        void getTokocashAccounts();

        void loginWithTokocash(AccountTokocash accountTokocash);
    }
}
