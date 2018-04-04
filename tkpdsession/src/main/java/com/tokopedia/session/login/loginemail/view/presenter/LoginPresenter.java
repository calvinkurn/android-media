package com.tokopedia.session.login.loginemail.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.di.SessionModule;
import com.tokopedia.session.R;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.session.login.loginemail.domain.interactor.LoginEmailUseCase;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginDiscoverSubscriber;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginSosmedSubscriber;
import com.tokopedia.session.login.loginemail.view.subscriber.LoginSubscriber;
import com.tokopedia.session.login.loginemail.view.viewlistener.Login;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWebviewUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWithSosmedUseCase;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.WebViewLoginFragment;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author by nisie on 12/18/17.
 */

public class LoginPresenter extends BaseDaggerPresenter<Login.View>
        implements Login.Presenter {

    private static final String LOGIN_CACHE_KEY = "LOGIN_ID";
    private static final String BUNDLE = "bundle";
    private static final String ERROR = "error";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String SERVER = "server";
    private static final String PATH = "path";
    private static final String HTTPS = "https://";
    private static final String ACTIVATION_SOCIAL = "activation-social";

    private final LocalCacheHandler loginCache;
    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final LoginWithSosmedUseCase loginWithSosmedUseCase;
    private final LoginWebviewUseCase loginWebviewUseCase;
    private final LoginEmailUseCase loginEmailUseCase;
    private final MakeLoginUseCase makeLoginUseCase;
    private final SessionHandler sessionHandler;

    @Inject
    public LoginPresenter(@Named(SessionModule.LOGIN_CACHE) LocalCacheHandler loginCache,
                          SessionHandler sessionHandler,
                          LoginEmailUseCase loginEmailUseCase,
                          DiscoverUseCase discoverUseCase,
                          GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                          LoginWithSosmedUseCase loginWithSosmedUseCase,
                          LoginWebviewUseCase loginWebviewUseCase,
                          MakeLoginUseCase makeLoginUseCase
    ) {
        this.sessionHandler = sessionHandler;
        this.loginCache = loginCache;
        this.loginEmailUseCase = loginEmailUseCase;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.loginWithSosmedUseCase = loginWithSosmedUseCase;
        this.loginWebviewUseCase = loginWebviewUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        loginEmailUseCase.unsubscribe();
        discoverUseCase.unsubscribe();
        loginWithSosmedUseCase.unsubscribe();
        loginWebviewUseCase.unsubscribe();
        makeLoginUseCase.unsubscribe();
    }

    @Override
    public void login(String email, String password) {
        Log.d("NISNIS", "login ");

        getView().resetError();
        if (isValid(email, password)) {
            getView().showLoadingLogin();
            getView().disableArrow();
            loginEmailUseCase.execute(LoginEmailUseCase.getParam(email, password),
                    new LoginSubscriber(getView(), email));
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
    public void loginWebview(Intent data) {
        if (data.getBundleExtra(BUNDLE) != null
                && data.getBundleExtra(BUNDLE).getString(PATH) != null) {
            Bundle bundle = data.getBundleExtra(BUNDLE);
            if (bundle.getString(PATH, "").contains(ERROR)) {
                getView().onErrorLogin(bundle.getString(MESSAGE, ""), ErrorCode.WS_ERROR);
            } else if (bundle.getString(PATH, "").contains(CODE)) {
                getView().showLoadingLogin();
                String name = bundle.getString(WebViewLoginFragment.NAME, "");
                loginWebviewUseCase.execute(LoginWebviewUseCase.getParamWebview(bundle.getString
                                (CODE, ""), HTTPS + bundle.getString(SERVER) + bundle.getString
                                (PATH)),
                        new LoginSosmedSubscriber(name, getView(), ""));
            } else if (bundle.getString(PATH, "").contains(ACTIVATION_SOCIAL)) {
                getView().onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
            }
        } else {
            getView().onErrorLogin(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }

    @Override
    public void loginGoogle(String accessToken, String email) {
        getView().showLoadingLogin();
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamGoogle(accessToken), new
                LoginSosmedSubscriber(LoginAnalytics.Label.GPLUS, getView(), email));
    }

    @Override
    public void getFacebookCredential(Fragment fragment, CallbackManager callbackManager) {
        getFacebookCredentialUseCase.execute(GetFacebookCredentialUseCase.getParam(
                fragment,
                callbackManager),
                new GetFacebookCredentialSubscriber(getView().getFacebookCredentialListener()));
    }

    @Override
    public void loginFacebook(AccessToken accessToken, String email) {
        getView().showLoadingLogin();
        loginWithSosmedUseCase.execute(LoginWithSosmedUseCase.getParamFacebook(accessToken),
                new LoginSosmedSubscriber(LoginAnalytics.Label.FACEBOOK, getView(), email));
    }

    @Override
    public void resetToken() {
        sessionHandler.clearToken();
    }

}
