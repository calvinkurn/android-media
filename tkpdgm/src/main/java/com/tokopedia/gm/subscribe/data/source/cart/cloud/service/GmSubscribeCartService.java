package com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GmSubscribeCartApi;

import retrofit2.Retrofit;

/**
 * Created by sebastianuskh on 3/13/17.
 */

public class GmSubscribeCartService extends AuthService<GmSubscribeCartApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GmSubscribeCartApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKOPEDIA_CART_DOMAIN;
    }

    @Override
    public GmSubscribeCartApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerWithClientDefaultAuth())
                .build();
    }
}
