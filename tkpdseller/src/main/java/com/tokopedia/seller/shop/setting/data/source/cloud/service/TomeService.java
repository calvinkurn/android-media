package com.tokopedia.seller.shop.setting.data.source.cloud.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.core.network.retrofit.services.BearerService;
import com.tokopedia.seller.shop.setting.data.source.cloud.api.TomeApi;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by Hendry on 3/22/2017.
 */

public class TomeService extends BearerService<TomeApi> {

    @Inject
    public TomeService(String mToken) {
        super(mToken);
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TomeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Tome.URL_ADDRESS;
    }

    @Override
    protected String getOauthAuthorization() {
        return mToken;
    }

    @Override
    public TomeApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientDefaultAuth())
                .build();
    }
}