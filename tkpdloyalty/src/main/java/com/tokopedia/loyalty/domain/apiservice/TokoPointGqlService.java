package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.services.BaseService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by sachinbansal on 5/15/18.
 */

public class TokoPointGqlService extends BaseService<TokoPointGqlApi> {


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoPointGqlApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public TokoPointGqlApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = OkHttpFactory.create()
                .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .getClientBuilder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor());
        tkpdOkHttpBuilder.addInterceptor(new TokoPointAuthInterceptor(TkpdBaseURL.TokoPoint.HMAC_KEY));
        tkpdOkHttpBuilder.setOkHttpRetryPolicy(getOkHttpRetryPolicy());
        tkpdOkHttpBuilder.addDebugInterceptor();
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return TokoPointRetrofitFactory.createRetrofitTokoPointConfig(processedBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}