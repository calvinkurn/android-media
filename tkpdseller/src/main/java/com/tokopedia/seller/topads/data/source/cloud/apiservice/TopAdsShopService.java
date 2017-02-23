package com.tokopedia.seller.topads.data.source.cloud.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.data.source.cloud.interceptor.TopAdsAuthInterceptor;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

public class TopAdsShopService extends AuthService<TopAdsShopApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsShopApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_SHOP;
    }

    @Override
    public TopAdsShopApi getApi() {
        return api;
    }

    @Override
    public Interceptor getAuthInterceptor() {
        return new TkpdAuthInterceptor();
    }
}
