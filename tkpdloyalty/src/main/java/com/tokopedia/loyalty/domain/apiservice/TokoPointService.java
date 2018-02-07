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
 * @author anggaprasetiyo on 27/11/17.
 */

public class TokoPointService extends BaseService<TokoPointApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TokoPointApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOKOPOINT_API_DOMAIN + TkpdBaseURL.TokoPoint.VERSION;
    }

    @Override
    public TokoPointApi getApi() {
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
        // tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
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
