package com.tokopedia.session.login.loginphonenumber.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.ChooseTokocashAccount;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.AccountTokocash;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 12/4/17.
 */

public class ChooseTokocashAccountPresenter extends BaseDaggerPresenter<ChooseTokocashAccount.View>
        implements ChooseTokocashAccount.Presenter {

    @Inject
    public ChooseTokocashAccountPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void getTokocashAccounts() {
        getView().onSuccessGetTokocashAccounts(new ArrayList<AccountTokocash>());
    }

    @Override
    public void loginWithTokocash(AccountTokocash accountTokocash) {
        getView().onSuccessLogin();
    }
}
