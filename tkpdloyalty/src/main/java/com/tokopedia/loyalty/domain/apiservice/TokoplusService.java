package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public class
TokoplusService extends BaseService<TokoplusApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoplusApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKOPLUS_API_DOMAIN + TkpdBaseURL.Tokoplus.VERSION; //TODO BASEURL + VERSION
    }

    @Override
    public TokoplusApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return TokoplusRetrofitFactory.createRetrofitTokoplusConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTokoplusAuth(TkpdBaseURL.Tokoplus.HMAC_KEY))

                .build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
