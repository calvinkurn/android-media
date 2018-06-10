package com.tokopedia.session.login.loginphonenumber.view.viewlistener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.otp.tokocashotp.view.viewmodel.LoginTokoCashViewModel;
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

        void goToSecurityQuestion(AccountTokocash email, LoginTokoCashViewModel loginTokoCashViewModel);

        void onForbidden();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<ChooseTokocashAccount.View> {

        void loginWithTokocash(String accessToken, AccountTokocash accountTokocash);
    }
}
