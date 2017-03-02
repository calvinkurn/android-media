package com.tokopedia.seller.topads.network.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.topads.network.apiservice.api.TopAdsManagementApi;
import com.tokopedia.core.network.retrofit.interceptors.TopAdsAuthInterceptor;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class TopAdsManagementService extends AuthService<TopAdsManagementApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsManagementApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOPADS_DOMAIN;
    }

    @Override
    public TopAdsManagementApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildCLientTopAdsAuth())
                .build();
    }
}
