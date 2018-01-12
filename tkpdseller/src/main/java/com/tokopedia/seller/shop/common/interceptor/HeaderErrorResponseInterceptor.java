package com.tokopedia.seller.shop.common.interceptor;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor;
import com.tokopedia.core.network.retrofit.response.BaseResponseError;

import okhttp3.Response;

/**
 * Use this Interceptor if the header error must be 422.
 * If the header is success, do not process the error.
 */

public class HeaderErrorResponseInterceptor extends TkpdErrorResponseInterceptor {

    public static final int HEADER_ERROR_CODE = 422;

    public HeaderErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass) {
        super(responseErrorClass);
    }

    @Override
    protected boolean mightContainCustomError(Response response) {
        return response != null && response.code() == HEADER_ERROR_CODE;
    }
}
