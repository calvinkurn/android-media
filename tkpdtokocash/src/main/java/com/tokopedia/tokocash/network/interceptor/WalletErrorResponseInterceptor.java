package com.tokopedia.tokocash.network.interceptor;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.network.WalletTokenRefresh;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nabillasabbaha on 1/25/18.
 */

public class WalletErrorResponseInterceptor implements Interceptor {

    public final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";
    private static final int BYTE_COUNT = 2048;

    private Class<? extends BaseResponseError> responseErrorClass;
    private AbstractionRouter abstractionRouter;
    private Gson gson;
    private BaseResponseError responseError;
    private WalletTokenRefresh walletTokenRefresh;
    private WalletUserSession walletUserSession;

    public WalletErrorResponseInterceptor(@NonNull Class<? extends BaseResponseError> responseErrorClass,
                                          AbstractionRouter abstractionRouter, Gson gson, WalletTokenRefresh walletTokenRefresh,
                                          WalletUserSession walletUserSession) {
        this.abstractionRouter = abstractionRouter;
        this.responseErrorClass = responseErrorClass;
        this.walletTokenRefresh = walletTokenRefresh;
        this.walletUserSession = walletUserSession;
        this.gson = gson;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (isUnauthorizeWalletToken(response)) {
            walletTokenRefresh.refreshToken();
            Request newRequestWallet = reCreateRequestWithNewAccessToken(chain);
            Response responseNew = chain.proceed(newRequestWallet);
            if (isUnauthorizeWalletToken(responseNew)) {
                throw responseError.createException();
            }
            return responseNew;
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

    private boolean isUnauthorizeWalletToken(Response response) {
        try {
            String responseString = response.peekBody(512).string();
            return (responseString.toLowerCase().contains("invalid token") ||
                    responseString.toLowerCase().contains("Invalid token"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Request reCreateRequestWithNewAccessToken(Chain chain) {
        return chain.request().newBuilder()
                .header(AUTHORIZATION, BEARER + " " + walletUserSession.getTokenWallet())
                .build();
    }
}
