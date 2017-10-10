package com.tokopedia.tkpdreactnative.react.data.datasource.interceptor;

import okhttp3.Request;

/**
 * Created by alvarisi on 10/9/17.
 */

public class ReactNativeBearerInterceptor extends ReactNativeInterceptor {
    private String token;

    public ReactNativeBearerInterceptor(String token) {
        this.token = token;
    }

    @Override
    protected Request buildNewRequest(Request request, Request.Builder builder) {
        builder.addHeader("Authorization", "Bearer " + token);
        return super.buildNewRequest(request, builder);
    }
}
