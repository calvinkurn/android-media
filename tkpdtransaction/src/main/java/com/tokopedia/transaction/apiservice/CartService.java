package com.tokopedia.transaction.apiservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.services.AuthService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public class CartService extends AuthService<CartApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        this.api = retrofit.create(CartApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_API_DOMAIN;
    }

    @Override
    public CartApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder builder = OkHttpFactory.create()
                .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .getClientBuilder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder);
        tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor());
        tkpdOkHttpBuilder.addInterceptor(new CartAuthInterceptor(TkpdBaseURL.Cart.HMAC_KEY));
        tkpdOkHttpBuilder.setOkHttpRetryPolicy(getOkHttpRetryPolicy());
        tkpdOkHttpBuilder.addDebugInterceptor();
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return new Retrofit.Builder()
                .baseUrl(processedBaseUrl)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    @Override
    protected OkHttpRetryPolicy getOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdOkHttpNoAutoRetryPolicy();
    }
}
