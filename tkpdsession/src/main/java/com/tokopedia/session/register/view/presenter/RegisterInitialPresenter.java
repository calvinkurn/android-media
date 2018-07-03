package com.tokopedia.session.register.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.analytics.LoginAnalytics;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.WebViewLoginFragment;
import com.tokopedia.session.domain.interactor.DiscoverUseCase;
import com.tokopedia.session.register.domain.interactor.RegisterValidationUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.GetFacebookCredentialUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWebviewUseCase;
import com.tokopedia.session.register.domain.interactor.registerinitial.LoginWithSosmedUseCase;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterDiscoverSubscriber;
import com.tokopedia.session.register.view.subscriber.registerinitial.RegisterSosmedSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterInitial;
import com.tokopedia.session.register.view.viewmodel.RegisterValidationViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

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
    private static final String PHONE_TYPE = "phone";
    private static final String EMAIL_TYPE = "email";

    private final DiscoverUseCase discoverUseCase;
    private final GetFacebookCredentialUseCase getFacebookCredentialUseCase;
    private final LoginWithSosmedUseCase loginSosmedUseCase;
    private final SessionHandler sessionHandler;
    private final LoginWebviewUseCase registerWebviewUseCase;
    private final RegisterValidationUseCase registerValidationUseCase;

    @Inject
    public RegisterInitialPresenter(SessionHandler sessionHandler,
                                    DiscoverUseCase discoverUseCase,
                                    GetFacebookCredentialUseCase getFacebookCredentialUseCase,
                                    LoginWithSosmedUseCase loginSosmedUseCase,
                                    LoginWebviewUseCase registerWebviewUseCase,
                                    RegisterValidationUseCase registerValidationUseCase) {
        this.sessionHandler = sessionHandler;
        this.discoverUseCase = discoverUseCase;
        this.getFacebookCredentialUseCase = getFacebookCredentialUseCase;
        this.loginSosmedUseCase = loginSosmedUseCase;
        this.registerWebviewUseCase = registerWebviewUseCase;
        this.registerValidationUseCase = registerValidationUseCase;
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
    public void validateRegister(String id) {
        registerValidationUseCase.execute(createValidateRegisterParam(id),
                new Subscriber<RegisterValidationViewModel>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                getView().onErrorValidateRegister(throwable.getMessage());
            }

            @Override
            public void onNext(RegisterValidationViewModel registerValidationViewModel) {
                onSuccessValidate(registerValidationViewModel);
            }
        });
    }

    private RequestParams createValidateRegisterParam(String id){
        RequestParams param = RequestParams.create();
        param.putString(RegisterValidationUseCase.PARAM_ID, id);
        return param;
    }

    private void onSuccessValidate(RegisterValidationViewModel model) {
        if (TextUtils.equals(model.getType(), PHONE_TYPE)){
            getView().setTempPhoneNumber(model.getView());
            if (model.getExist()){
                getView().showRegisteredPhoneDialog(model.getView());
            } else {
                getView().showProceedWithPhoneDialog(model.getView());
            }
        }

        if (TextUtils.equals(model.getType(), EMAIL_TYPE)){
            if (model.getExist()){
                getView().showRegisteredEmailDialog(model.getView());
            }
            else {
                getView().goToRegisterEmailPageWithEmail(model.getView());
            }
        }
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
