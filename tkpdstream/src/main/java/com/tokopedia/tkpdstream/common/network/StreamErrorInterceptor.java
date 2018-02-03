package com.tokopedia.tkpdstream.common.network;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

/**
 * @author by nisie on 2/3/18.
 */

public class StreamErrorInterceptor extends ErrorResponseInterceptor {

    public StreamErrorInterceptor(@NonNull Class<StreamErrorResponse> responseErrorClass) {
        super(responseErrorClass);
    }
}
