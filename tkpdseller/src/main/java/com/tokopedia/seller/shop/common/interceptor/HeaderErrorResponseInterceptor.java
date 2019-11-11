package com.tokopedia.seller.shop.common.interceptor;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;

import okhttp3.Response;

/**
 * Use this Interceptor if the header error must be 422.
 * If the header is success, do not process the error.
 */

public class HeaderErrorResponseInterceptor extends ErrorResponseInterceptor {

    public static final int HEADER_ERROR_CODE = 422;

    public HeaderErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        super(responseErrorClass);
    }

    @Override
    protected boolean mightContainCustomError(Response response) {
        return response != null && response.code() == HEADER_ERROR_CODE;
    }
}
