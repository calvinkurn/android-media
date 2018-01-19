package com.tokopedia.mitratoppers.common.interceptor;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

import okhttp3.Response;

/**
 * If the header is success, do not process the error.
 */

public class HeaderErrorResponseInterceptor extends ErrorResponseInterceptor {

    public static final int HEADER_SUCCESS_CODE = 200;

    public HeaderErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        super(responseErrorClass);
    }

    @Override
    protected boolean mightContainCustomError(Response response) {
        return response != null && response.code() != HEADER_SUCCESS_CODE;
    }
}
