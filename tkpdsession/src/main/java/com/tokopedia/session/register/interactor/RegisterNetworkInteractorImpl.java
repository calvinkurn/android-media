package com.tokopedia.session.register.interactor;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.model.gson.ValidateEmailResult;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterNetworkInteractorImpl implements RegisterNetworkInteractor, RegisterConstant {

    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";
    private AccountsService accountsService;

    public RegisterNetworkInteractorImpl(AccountsService accountsService) {
        this.accountsService = accountsService;
    }


    @Override
    public Observable<Response<TkpdResponse>> getValidateEmailObservable(TKPDMapParam<String, String> param) {
        return accountsService.getApi().validateEmail(param);
    }

    @Override
    public Observable<Integer> smartRegister(final TKPDMapParam<String, String> param) {
        return getValidateEmailObservable(param)
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            final TkpdResponse tkpdResponse = response.body();
                            if (!tkpdResponse.isError()
                                    && !response.body().getStatus().equals(TOO_MANY_REQUEST)
                                    && response.body().getErrorMessages() != null
                                    && response.body().getErrorMessages().size() != 0) {
                                ValidateEmailResult result = response.body().convertDataObj(ValidateEmailResult.class);
                                switch (result.getAction()) {
                                    case GO_TO_REGISTER:
                                        return goToRegister();
                                    case GO_TO_ACTIVATION_PAGE:
                                        return goToActivationPage();
                                    case GO_TO_LOGIN:
                                        return goToLogin();
                                    case GO_TO_RESET_PASSWORD:
                                        return goToResetPassword();
                                    default:
                                        return goToRegister();
                                }
                            } else {
                                throw new RuntimeException(response.body().getErrorMessageJoined());
                            }
                        } else {
                            throw new RuntimeException(String.valueOf(response.code()));
                        }
                    }
                });
    }

    private Observable<Integer> goToRegister() {
        int action = GO_TO_REGISTER;
        return Observable.just(action);
    }

    private Observable<Integer> goToLogin() {
        int action = GO_TO_LOGIN;
        return Observable.just(action);
    }

    private Observable<Integer> goToActivationPage() {
        int action = GO_TO_ACTIVATION_PAGE;
        return Observable.just(action);
    }

    private Observable<Integer> goToResetPassword() {
        int action = GO_TO_RESET_PASSWORD;
        return Observable.just(action);
    }

    @Override
    public Observable<Response<TkpdResponse>> doRegister(TKPDMapParam<String, String> param) {
        return accountsService.getApi().doRegister(param);
    }

}
