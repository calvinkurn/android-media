package com.tokopedia.seller.topads.data.source.cloud.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.data.source.cloud.interceptor.TopAdsAuthInterceptor;

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
    public Interceptor getAuthInterceptor() {
        return new TopAdsAuthInterceptor();
    }
}
