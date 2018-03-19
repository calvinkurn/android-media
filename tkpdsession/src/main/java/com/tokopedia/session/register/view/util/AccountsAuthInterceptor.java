package com.tokopedia.session.register.view.util;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by yfsx on 19/03/18.
 */

public class AccountsAuthInterceptor implements Interceptor {

    private static final String KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String BEARER = "Bearer";

    public AccountsAuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest.addHeader(KEY_ACCOUNTS_AUTHORIZATION, generateHeadersAccount());
        return chain.proceed(newRequest.build());
    }

    private String generateHeadersAccount() {
        String clientID = "7ea919182ff";
        String clientSecret = "b36cbf904d14bbf90e7f25431595a364";
        String encodeString = clientID + ":" + clientSecret;
        String asB64 = Base64.encodeToString(encodeString.getBytes(), Base64.NO_WRAP);
        return "Basic " + asB64;
    }
}
