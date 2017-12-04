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
TokoPointService extends BaseService<TokoPointApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoPointApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKOPOINT_API_DOMAIN + TkpdBaseURL.TokoPoint.VERSION; //TODO BASEURL + VERSION
    }

    @Override
    public TokoPointApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return TokoPointRetrofitFactory.createRetrofitTokoPointConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTokoplusAuth(TkpdBaseURL.TokoPoint.HMAC_KEY))

                .build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
