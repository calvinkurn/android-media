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
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.RegisterFacebookUseCase;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterDiscoverSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterFacebookSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;

import javax.inject.Inject;

/**
 * @author by nisie on 10/10/17.
 */

public class RegisterInitialPresenter extends BaseDaggerPresenter<RegisterInitial.View>
        implements RegisterInitial.Presenter {

    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final RegisterFacebookUseCase registerFacebookUseCase;
    private final SessionHandler sessionHandler;
    private RegisterInitial.View viewListener;

    @Inject
    public RegisterInitialPresenter(SessionHandler sessionHandler,
                                    DiscoverUseCase discoverUseCase,
                                    GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                                    RegisterFacebookUseCase registerFacebookUseCase) {
        this.sessionHandler = sessionHandler;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.registerFacebookUseCase = registerFacebookUseCase;
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
        registerFacebookUseCase.unsubscribe();
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
        registerFacebookUseCase.execute(
                RegisterFacebookUseCase.getParam(accessToken),
                new RegisterFacebookSubscriber(viewListener)
        );
    }

    @Override
    public void clearToken() {
        sessionHandler.clearUserData(MainApplication.getAppContext());
    }
}
