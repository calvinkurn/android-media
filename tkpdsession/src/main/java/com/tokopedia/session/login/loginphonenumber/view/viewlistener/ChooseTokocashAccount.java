package com.tokopedia.session.login.loginphonenumber.view.viewlistener;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

/**
 * @author by nisie on 12/4/17.
 */

public interface ChooseTokocashAccount {
    interface View extends CustomerView {

        void onSelectedTokocashAccount(AccountTokocash accountTokocash);

        void onSuccessLogin();

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorLoginTokoCash(String errorMessage);

        void goToSecurityQuestion(AccountTokocash email, MakeLoginDomain makeLoginDomain);
    }

    interface Presenter extends CustomerPresenter<ChooseTokocashAccount.View> {

        void loginWithTokocash(String accessToken, AccountTokocash accountTokocash);

        void clearUserData();
    }
}
