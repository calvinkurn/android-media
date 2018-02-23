package com.tokopedia.transaction.apiservice;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

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
        Log.d("CARTAPI ERROR = ", responseError);
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
        Log.d("CARTAPI PATH = ", path);
        Log.d("CARTAPI PARAM QUERY = ", strParam);
        Log.d("CARTAPI METHOD = ", method);
        Log.d("CARTAPI AUTH KEY = ", authKey);
        Log.d("CARTAPI CONTENT TYPE = ", contentTypeHeader);
        Map<String, String> mapHeader = AuthUtil.getDefaultHeaderMap(
                path,
                strParam,
                method,
                contentTypeHeader != null ? contentTypeHeader : "application/x-www-form-urlencoded",
                authKey,
                "EEE, dd MMM yyyy HH:mm:ss ZZZ");

        mapHeader.put("X-APP-VERSION", GlobalConfig.VERSION_NAME);
        mapHeader.put("Tkpd-UserId", SessionHandler.getLoginID(MainApplication.getAppContext()));
        mapHeader.put("X-Device", "android");
        mapHeader.put("Tkpd-SessionId", GCMHandler.getRegistrationId(MainApplication.getAppContext()));

        for (Map.Entry<String, String> entry : mapHeader.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("CARTAPI HEADER = ", "KEY = " + key + "| VALUE = " + value);
        }

        return mapHeader;
    }
}
