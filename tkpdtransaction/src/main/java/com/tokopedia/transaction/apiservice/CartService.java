package com.tokopedia.transaction.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public class CartService extends AuthService<CartApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        this.api = retrofit.create(CartApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_DOMAIN;
    }

    @Override
    public CartApi getApi() {
        return api;
    }
}
