package com.tokopedia.tokocash.network.interceptor;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.tokocash.network.exception.UserInactivateTokoCashException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nabillasabbaha on 1/25/18.
 */

public class TokoCashErrorResponseInterceptor implements Interceptor {

    private static final int BYTE_COUNT = 2048;

    private Class<? extends BaseResponseError> responseErrorClass;
    private Gson gson;
    private BaseResponseError responseError;

    public TokoCashErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass, Gson gson) {
        this.responseErrorClass = responseErrorClass;
        this.gson = gson;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (isTokoCashInactivate(response)) {
            throw new UserInactivateTokoCashException("User is not activate tokocash yet");
        }

        ResponseBody responseBody = null;
        String responseBodyString = "";
        if (null != response) {
            responseBody = response.peekBody(BYTE_COUNT);
            responseBodyString = responseBody.string();

            responseError = null;
            try {
                responseError = gson.fromJson(responseBodyString, responseErrorClass);
            } catch (JsonSyntaxException e) { // the json might not be TkpdResponseError instance, so just return it
                return response;
            }
            if (responseError == null) { // no error object
                return response;
            } else {
                if (responseError.hasBody()) {
                    CommonUtils.dumper(response.headers().toString());
                    CommonUtils.dumper(responseBodyString);
                    response.body().close();
                    throw responseError.createException();
                } else {
                    return response;
                }
            }
        } else {
            return null;
        }
    }

    private boolean isTokoCashInactivate(Response response) {
        return response.code() == 402;
    }
}
