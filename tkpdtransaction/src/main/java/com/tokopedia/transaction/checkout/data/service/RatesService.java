package com.tokopedia.transaction.checkout.data.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Irfan Khoirul on 22/03/18.
 */

public class RatesService extends AuthService<RatesApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(RatesApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.KERO_RATES_DOMAIN;
    }

    @Override
    public RatesApi getApi() {
        return api;
    }

}
