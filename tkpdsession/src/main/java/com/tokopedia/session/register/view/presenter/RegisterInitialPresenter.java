package com.tokopedia.session.register.view.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
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

    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final RegisterWithSosmedUseCase registerSosmedUseCase;
    private final SessionHandler sessionHandler;
    private RegisterInitial.View viewListener;

    @Inject
    public RegisterInitialPresenter(SessionHandler sessionHandler,
                                    DiscoverUseCase discoverUseCase,
                                    GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                                    RegisterWithSosmedUseCase registerSosmedUseCase) {
        this.sessionHandler = sessionHandler;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.registerSosmedUseCase = registerSosmedUseCase;
    }

    @Override
    public void attachView(RegisterInitial.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void getProvider() {
        viewListener.showLoadingDiscover();
        discoverUseCase.execute(RequestParams.EMPTY, new RegisterDiscoverSubscriber(viewListener));
    }

    @Override
    public void detachView() {
        super.detachView();
        discoverUseCase.unsubscribe();
        registerSosmedUseCase.unsubscribe();
    }

    @Override
    public void registerWebview(FragmentActivity activity, Bundle data) {
        Bundle bundle = data;
        bundle.putBoolean(DownloadService.IS_NEED_LOGIN, false);

        viewListener.showProgressBar();
//        ((SessionView) context).sendDataFromInternet(DownloadService.LOGIN_WEBVIEW, bundle);
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
        registerSosmedUseCase.execute(
                RegisterWithSosmedUseCase.getParamFacebook(accessToken),
                new RegisterSosmedSubscriber(viewListener)
        );
    }

    @Override
    public void clearToken() {
        sessionHandler.clearUserData(MainApplication.getAppContext());
    }

    @Override
    public void registerGoogle(LoginGoogleModel model) {
        registerSosmedUseCase.execute(
                RegisterWithSosmedUseCase.getParamGoogle(model.getAccessToken()),
                new RegisterSosmedSubscriber(viewListener)
        );
    }
}
