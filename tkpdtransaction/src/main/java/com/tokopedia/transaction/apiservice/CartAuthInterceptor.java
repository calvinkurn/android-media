package com.tokopedia.transaction.apiservice;

import com.google.gson.Gson;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartAuthInterceptor extends TkpdAuthInterceptor {

    CartAuthInterceptor(String hmacKey) {
        super(hmacKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.body().string();
        if (responseError != null && !responseError.isEmpty() && responseError.contains("header")) {
            CartErrorResponse cartErrorResponse = new Gson().fromJson(
                    responseError, CartErrorResponse.class
            );
            if (cartErrorResponse.getCartHeaderResponse() != null) {
                throw new CartResponseErrorException(
                        response.code(),
                        cartErrorResponse.getCartHeaderResponse());
            }
        }
        throw new HttpErrorException(response.code());
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        return AuthUtil.generateHeaderCartCheckout(path, strParam, method, authKey, contentTypeHeader);
    }
}
