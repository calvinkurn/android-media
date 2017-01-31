package com.tokopedia.session.register.interactor;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.model.gson.ValidateEmailResult;
import com.tokopedia.session.session.intentservice.RegisterService;

import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterNetworkInteractorImpl implements RegisterNetworkInteractor, RegisterConstant{

    AccountsService accountsService;

    public RegisterNetworkInteractorImpl(AccountsService accountsService) {
        this.accountsService = accountsService;
    }


    @Override
    public Observable<Response<TkpdResponse>> getValidateEmailObservable(TKPDMapParam<String, String> param) {
        return accountsService.getApi().validateEmail(param);
    }

    @Override
    public Observable<Integer> smartRegister(TKPDMapParam<String, String> param) {
        return getValidateEmailObservable(param)
                .flatMap(new Func1<Response<TkpdResponse>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            final TkpdResponse tkpdResponse = response.body();
                            if (!tkpdResponse.isError()) {
                                ValidateEmailResult result = response.body().convertDataObj(ValidateEmailResult.class);
                                switch(result.getAction()){
                                    case GO_TO_REGISTER:
                                        return goToRegister(response);
                                    case GO_TO_ACTIVATION_PAGE:
                                        return goToActivationPage(response);
                                    case GO_TO_LOGIN:
                                        return goToLogin(response);
                                    case GO_TO_RESET_PASSWORD:
                                        return goToResetPassword(response);
                                    default:
                                        return goToRegister(response);
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

    private Observable<Integer> goToActivationPage(Response<TkpdResponse> response) {
        int action = GO_TO_ACTIVATION_PAGE;
        return Observable.just(action);
    }

    private Observable<Integer> goToResetPassword(Response<TkpdResponse> response) {
        int action = GO_TO_RESET_PASSWORD;
        return Observable.just(action);
    }

    @Override
    public Observable<Response<TkpdResponse>> finishRegister(TKPDMapParam<String, String> param) {
        return accountsService.getApi().doRegister(param);
    }

    @Override
    public Observable<Response<TkpdResponse>> resendActivation(TKPDMapParam<String, String> param) {
        return accountsService.getApi().resentActivation(param);
    }

    private Observable<Integer> goToRegister(Response<TkpdResponse> tkpdResponse) {
        int action = GO_TO_REGISTER;
        return Observable.just(action);
    }

    private TKPDMapParam<String, String> getLoginParam() {
        return new TKPDMapParam<>();
    }

    private Observable<Integer> goToLogin(Response<TkpdResponse> loginParam) {
        int action = GO_TO_LOGIN;
        return Observable.just(action);
//                .flatMap(new Func1<TKPDMapParam<String, String>, Observable<Response<TkpdResponse>>>() {
//                    @Override
//                    public Observable<Response<TkpdResponse>> call(TKPDMapParam<String, String> stringStringTKPDMapParam) {
//                        return null;
//                    }
//                });
    }
}
