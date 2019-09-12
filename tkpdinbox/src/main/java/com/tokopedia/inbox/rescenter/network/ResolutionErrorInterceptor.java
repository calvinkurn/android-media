package com.tokopedia.inbox.rescenter.network;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

/**
 * @author by yfsx on 26/07/18.
 */

public class ResolutionErrorInterceptor extends ErrorResponseInterceptor {

    public ResolutionErrorInterceptor(@NonNull Class<ResolutionErrorResponse> responseErrorClass) {
        super(responseErrorClass);
    }
}
