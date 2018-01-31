package com.tokopedia.network.service;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.network.SessionUrl;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * @author by nisie on 12/27/17.
 */

public class AccountsBasicService extends AuthService<AccountsBasicApi> {

    @Inject
    public AccountsBasicService() {
        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildBasicAuth())
                .build());
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(AccountsBasicApi.class);

    }

    @Override
    protected String getBaseUrl() {
        return SessionUrl.ACCOUNTS_DOMAIN;
    }

    @Override
    public AccountsBasicApi getApi() {
        return api;
    }
}
