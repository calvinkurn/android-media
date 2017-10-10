package com.tokopedia.tkpdreactnative.react.data.datasource.interceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by alvarisi on 10/9/17.
 */

public class ReactNativeWsAuthInterceptor extends ReactNativeInterceptor {
    public ReactNativeWsAuthInterceptor() {
    }

    @Override
    protected Request buildNewRequest(Request request, Request.Builder builder) {
        Map<String, String> authHeaders = new HashMap<>();
        authHeaders = prepareHeader(authHeaders, request);
        for (Map.Entry<String, String> header : authHeaders.entrySet()) {
            builder.addHeader(header.getKey(), String.valueOf(header.getValue()));
        }
        return super.buildNewRequest(request, builder);
    }
}