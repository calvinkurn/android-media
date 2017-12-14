package com.tokopedia.loyalty.domain.apiservice;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.loyalty.domain.entity.response.TokoPointErrorResponse;
import com.tokopedia.loyalty.exception.TokoPointResponseErrorException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class TokoPointAuthInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = TokoPointAuthInterceptor.class.getSimpleName();

    TokoPointAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.body().string();
        if (responseError != null && !responseError.isEmpty() && responseError.contains("header")) {
            TokoPointErrorResponse tokoPointErrorResponse = new Gson().fromJson(
                    responseError, TokoPointErrorResponse.class
            );
            if (tokoPointErrorResponse.getHeaderResponse() != null) {
                throw new TokoPointResponseErrorException(
                        response.code(),
                        tokoPointErrorResponse.getHeaderResponse());
            }
        }
        throw new HttpErrorException(response.code());
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader
    ) {
        return AuthUtil.generateHeadersWithPath(
                path, strParam, method, authKey, contentTypeHeader
        );
    }
}
