package com.tokopedia.session.register.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorCode;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.RegisterWebviewUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.RegisterWithSosmedUseCase;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterDiscoverSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterSosmedSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

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
    private final RegisterWithSosmedUseCase registerSosmedUseCase;
    private final SessionHandler sessionHandler;
    private final RegisterWebviewUseCase registerWebviewUseCase;
    private RegisterInitial.View viewListener;

    @Inject
    public RegisterInitialPresenter(SessionHandler sessionHandler,
                                    DiscoverUseCase discoverUseCase,
                                    GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                                    RegisterWithSosmedUseCase registerSosmedUseCase,
                                    RegisterWebviewUseCase registerWebviewUseCase) {
        this.sessionHandler = sessionHandler;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.registerSosmedUseCase = registerSosmedUseCase;
        this.registerWebviewUseCase = registerWebviewUseCase;
    }

    @Override
    public void attachView(RegisterInitial.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void getProvider() {
        viewListener.showLoadingDiscover();
        discoverUseCase.execute(DiscoverUseCase.getParamRegister(), new RegisterDiscoverSubscriber
                (viewListener));
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
        registerSosmedUseCase.unsubscribe();
        registerWebviewUseCase.unsubscribe();
    }

    @Override
    public void registerWebview(FragmentActivity activity, Intent data) {
        viewListener.showProgressBar();

        Bundle bundle = data.getBundleExtra(BUNDLE_WEBVIEW);
        if (bundle != null && bundle.getString(ARGS_PATH).contains(ARGS_ERROR)) {
            viewListener.dismissProgressBar();
            viewListener.onErrorRegisterSosmed(bundle.getString(ARGS_MESSAGE) + " " + ErrorCode
                    .EMPTY_ACCESS_TOKEN);
        } else if (bundle != null
                && bundle.getString(ARGS_PATH) != null
                && bundle.getString(ARGS_CODE) != null
                && bundle.getString(ARGS_SERVER) != null) {
            registerWebviewUseCase.execute(RegisterWebviewUseCase.getParamWebview(
                    bundle.getString(ARGS_CODE),
                    HTTPS + bundle.getString(ARGS_SERVER) + bundle.getString(ARGS_PATH),
                    sessionHandler.getTempLoginSession(MainApplication.getAppContext())
            ), new RegisterSosmedSubscriber(viewListener));
        } else {
            viewListener.dismissProgressBar();
            viewListener.onErrorRegisterSosmed(
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
                new GetFacebookCredentialSubscriber(viewListener));
    }

    @Override
    public void registerFacebook(AccessToken accessToken) {
        viewListener.showProgressBar();
        registerSosmedUseCase.execute(
                RegisterWithSosmedUseCase.getParamFacebook(accessToken,
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
                new RegisterSosmedSubscriber(viewListener)
        );
    }

    @Override
    public void clearToken() {
        sessionHandler.clearUserData(MainApplication.getAppContext());
    }

    @Override
    public void registerGoogle(LoginGoogleModel model) {
        viewListener.showProgressBar();
        registerSosmedUseCase.execute(
                RegisterWithSosmedUseCase.getParamGoogle(model.getAccessToken(),
                        sessionHandler.getTempLoginSession(MainApplication.getAppContext())),
                new RegisterSosmedSubscriber(viewListener)
        );
    }
}
