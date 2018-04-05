package com.tokopedia.session.register.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWebviewUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWithSosmedUseCase;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterDiscoverSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterSosmedSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;
import com.tokopedia.session.WebViewLoginFragment;

import javax.inject.Inject;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterInitialPresenter extends BaseDaggerPresenter<RegisterInitial.View>
        implements RegisterInitial.Presenter {

    private static final String BUNDLE_WEBVIEW = "bundle";
    private static final String ARGS_PATH = "path";
    private static final String ARGS_ERROR = "error";
    private static final String ARGS_MESSAGE = "message";
    private static final String ARGS_CODE = "code";
    private static final String ARGS_SERVER = "server";
    private static final String HTTPS = "https://";

    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final LoginWithSosmedUseCase loginSosmedUseCase;
    private final SessionHandler sessionHandler;
    private final LoginWebviewUseCase registerWebviewUseCase;

    @Inject
    public RegisterInitialPresenter(SessionHandler sessionHandler,
                                    DiscoverUseCase discoverUseCase,
                                    GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                                    LoginWithSosmedUseCase loginSosmedUseCase,
                                    LoginWebviewUseCase registerWebviewUseCase) {
        this.sessionHandler = sessionHandler;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.loginSosmedUseCase = loginSosmedUseCase;
        this.registerWebviewUseCase = registerWebviewUseCase;
    }

    @Override
    public void getProvider() {
        getView().showLoadingDiscover();
        discoverUseCase.execute(DiscoverUseCase.getParamRegister(), new RegisterDiscoverSubscriber
                (getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
        loginSosmedUseCase.unsubscribe();
        registerWebviewUseCase.unsubscribe();
    }

    @Override
    public void registerWebview(Intent data) {
        getView().showProgressBar();

        Bundle bundle = data.getBundleExtra(BUNDLE_WEBVIEW);
        if (bundle != null && bundle.getString(ARGS_PATH).contains(ARGS_ERROR)) {
            getView().dismissProgressBar();
            getView().onErrorRegisterSosmed(bundle.getString(ARGS_MESSAGE) + " " + ErrorCode
                    .EMPTY_ACCESS_TOKEN);
        } else if (bundle != null
                && bundle.getString(ARGS_PATH) != null
                && bundle.getString(ARGS_CODE) != null
                && bundle.getString(ARGS_SERVER) != null) {
            String name = bundle.getString(WebViewLoginFragment.NAME, "");
            registerWebviewUseCase.execute(LoginWebviewUseCase.getParamWebview(
                    bundle.getString(ARGS_CODE),
                    HTTPS + bundle.getString(ARGS_SERVER) + bundle.getString(ARGS_PATH)
            ), new RegisterSosmedSubscriber(name, getView()));
        } else {
            getView().dismissProgressBar();
            getView().onErrorRegisterSosmed(
                    ErrorHandler.getDefaultErrorCodeMessage(ErrorCode
                            .EMPTY_ACCESS_TOKEN));
        }
    }

    @Override
    public void getFacebookCredential(Fragment fragment, CallbackManager
            callbackManager) {
        getFacebookCredentialUseCase.execute(
                GetFacebookCredentialUseCase.getParam(
                        fragment,
                        callbackManager),
                new GetFacebookCredentialSubscriber(getView().getFacebookCredentialListener()));
    }

    @Override
    public void registerFacebook(AccessToken accessToken) {
        getView().showProgressBar();
        loginSosmedUseCase.execute(
                LoginWithSosmedUseCase.getParamFacebook(accessToken),
                new RegisterSosmedSubscriber(LoginAnalytics.Label.FACEBOOK, getView())
        );
    }

    @Override
    public void registerGoogle(String accessToken) {
        getView().showProgressBar();
        loginSosmedUseCase.execute(
                LoginWithSosmedUseCase.getParamGoogle(accessToken),
                new RegisterSosmedSubscriber(LoginAnalytics.Label.GPLUS, getView())
        );
    }
}
