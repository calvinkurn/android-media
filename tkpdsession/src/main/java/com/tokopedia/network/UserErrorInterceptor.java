package com.tokopedia.network;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

/**
 * @author by yfsx on 09/04/18.
 */

public class UserErrorInterceptor extends ErrorResponseInterceptor {

    public UserErrorInterceptor(@NonNull Class<UserErrorResponse> responseErrorClass) {
        super(responseErrorClass);
    }
}
