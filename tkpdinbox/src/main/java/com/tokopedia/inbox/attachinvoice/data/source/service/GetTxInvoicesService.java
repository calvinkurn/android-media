package com.tokopedia.inbox.attachinvoice.data.source.service;

import com.google.gson.Gson;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.inbox.attachinvoice.data.source.api.GetTxInvoicesApi;
import com.tokopedia.inbox.attachproduct.data.source.api.TomeGetShopProductAPI;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hendri on 21/03/18.
 */

public class GetTxInvoicesService extends AuthService<GetTxInvoicesApi>{
    @Override
    protected void initApiService(Retrofit retrofit) {
            api = retrofit.create(GetTxInvoicesApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return "http://10.0.11.130";
    }

    @Override
    public GetTxInvoicesApi getApi() {
        return api;
    }

//    @Override
//    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
//        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
//                .client(buildClientDefaultAuth())
//                .build();
//    }
//
//    public OkHttpClient buildClientDefaultAuth() {
//        OkHttpClient.Builder builder = getDefaultClient().newBuilder();
//        return new TkpdOkHttpBuilder(builder)
//                .addInterceptor(new FingerprintInterceptor())
//                .addInterceptor(new CacheApiInterceptor())
//                .addInterceptor(new TkpdAuthInterceptor())
//                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
//                .addDebugInterceptor()
//                .build();
//    }
//    private OkHttpClient getDefaultClient() {
//        return new OkHttpClient.Builder().build();
//    }
}
