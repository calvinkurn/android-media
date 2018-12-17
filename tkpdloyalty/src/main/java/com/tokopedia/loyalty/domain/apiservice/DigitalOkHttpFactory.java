package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.DigitalHmacAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;

import okhttp3.OkHttpClient;

public class DigitalOkHttpFactory extends OkHttpFactory {
    public OkHttpClient buildClientDigitalAuth(String authorizationString) {
        return new TkpdOkHttpBuilder(builder)
                .addInterceptor(new FingerprintInterceptor(MainApplication.getAppContext()))
                .addInterceptor(new DigitalHmacAuthInterceptor(authorizationString))
                .addInterceptor(getHttpLoggingInterceptor())
                .setOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .addDebugInterceptor()
                .build();
    }
}
