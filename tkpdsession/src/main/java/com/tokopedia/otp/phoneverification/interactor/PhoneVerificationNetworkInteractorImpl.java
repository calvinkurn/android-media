package com.tokopedia.otp.phoneverification.interactor;

import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by nisie on 2/23/17.
 */

public class PhoneVerificationNetworkInteractorImpl implements PhoneVerificationNetworkInteractor {

    private final AccountsService accountsService;

    public PhoneVerificationNetworkInteractorImpl(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Override
    public Observable<Response<TkpdResponse>> requestOTP(String userId, TKPDMapParam<String, String> param) {
        return accountsService.getApi().requestOtp(userId, param);
    }

    @Override
    public Observable<Response<TkpdResponse>> verifyOTP(TKPDMapParam<String, String> param) {
        return accountsService.getApi().validateOtp(param);
    }
}
