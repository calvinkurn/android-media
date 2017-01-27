package com.tokopedia.session.register.interactor;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.register.RegisterConstant;

import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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
                                JSONObject result = tkpdResponse.getJsonData();
                                boolean isActive = "1".equals(result.optString("email_status"));
                                if (isActive)
                                    return goToLogin(response);
                                else
                                    return goToRegister(response);
                            } else {
                                throw new RuntimeException(response.body().getErrorMessageJoined());
                            }
                        } else {
                            throw new RuntimeException(String.valueOf(response.code()));
                        }
                    }
                });
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
