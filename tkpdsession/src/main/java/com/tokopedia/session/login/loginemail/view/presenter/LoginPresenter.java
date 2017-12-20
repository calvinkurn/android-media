package com.tokopedia.session.login.loginemail.view.presenter;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.login.loginemail.domain.interactor.LoginEmailUseCase;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginDiscoverSubscriber;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginSubscriber;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginPresenter extends BaseDaggerPresenter<Login.View>
        implements Login.Presenter {

    private static final String LOGIN_CACHE_KEY = "LOGIN_ID";

    private final LoginEmailUseCase loginEmailUseCase;
    private final LocalCacheHandler loginCache;
    private final DiscoverUseCase discoverUseCase;

    @Inject
    public LoginPresenter(LoginEmailUseCase loginEmailUseCase,
                          DiscoverUseCase discoverUseCase,
                          @Named(SessionModule.LOGIN_CACHE) LocalCacheHandler loginCache) {
        this.loginEmailUseCase = loginEmailUseCase;
        this.discoverUseCase = discoverUseCase;
        this.loginCache = loginCache;
    }


    @Override
    public void detachView() {
        super.detachView();
        loginEmailUseCase.unsubscribe();
        discoverUseCase.unsubscribe();
    }

    @Override
    public void login(String email, String password) {
        getView().resetError();
        if (isValid(email, password)) {
            getView().showLoadingLogin();
            loginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    new LoginSubscriber(getView()));
        }
    }

    private boolean isValid(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(password)) {
            getView().showErrorPassword(R.string.error_field_required);
            isValid = false;
        } else if (password.length() < 4) {
            getView().showErrorPassword(R.string.error_incorrect_password);
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            getView().showErrorEmail(R.string.error_field_required);
            isValid = false;
        } else if (!CommonUtils.EmailValidation(email)) {
            getView().showErrorEmail(R.string.error_invalid_email);
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void saveLoginEmail(String email) {
        ArrayList<String> listId = loginCache.getArrayListString(LOGIN_CACHE_KEY);
        if (!TextUtils.isEmpty(email) && !listId.contains(email)) {
            listId.add(email);
            loginCache.putArrayListString(LOGIN_CACHE_KEY, listId);
            loginCache.applyEditor();
            getView().setAutoCompleteAdapter(listId);
        }
    }

    @Override
    public ArrayList<String> getLoginIdList() {
        return loginCache.getArrayListString(LOGIN_CACHE_KEY);
    }

    @Override
    public void discoverLogin() {
        getView().showLoadingDiscover();
        discoverUseCase.execute(RequestParams.EMPTY, new LoginDiscoverSubscriber(getView()));

    }

    @Override
    public void loginWebview() {

    }

    @Override
    public void loginGoogle() {

    }

    @Override
    public void loginFacebook() {

    }
}
