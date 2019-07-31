package com.tokopedia.session.register.view.util;


import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.tokopedia.abstraction.common.utils.network.AuthUtil.HEADER_AUTHORIZATION;

/**
 * @author by yfsx on 19/03/18.
 */

public class AccountsAuthInterceptor implements Interceptor {

    private static final String KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";

    public AccountsAuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest.removeHeader(KEY_ACCOUNTS_AUTHORIZATION);
        newRequest.addHeader(KEY_ACCOUNTS_AUTHORIZATION, generateHeadersAccount());
        return chain.proceed(newRequest.build());
    }

    private String generateHeadersAccount() {
        return AuthUtil.generateHeadersAccount("").get(HEADER_AUTHORIZATION);
    }
}
