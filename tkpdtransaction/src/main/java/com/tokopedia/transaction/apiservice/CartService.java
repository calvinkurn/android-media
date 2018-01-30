package com.tokopedia.transaction.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.services.AuthService;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        TkpdOkHttpBuilder tkpdOkHttpBuilder = OkHttpFactory.create()
                .buildClientDefaultAuthBuilder()
                .addInterceptor(new CartAuthInterceptor(TkpdBaseURL.Cart.HMAC_KEY))
                .addInterceptor(loggingInterceptor);

        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(tkpdOkHttpBuilder.build())
                .addConverterFactory(new CartResponseConverter())
                .build();
    }


}
