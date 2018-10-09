package com.tokopedia.session.forgotpassword.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordRetrofitInteractorImpl implements ForgotPasswordRetrofitInteractor {

    private final AccountsService accountsService;

    public ForgotPasswordRetrofitInteractorImpl(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Override
    public Observable<Response<TkpdResponse>> resetPassword(TKPDMapParam<String, String> param) {
        return accountsService.getApi().resetPassword(
                AuthUtil.generateParamsNetwork(
                        MainApplication.getAppContext(),
                        param));
    }
}
