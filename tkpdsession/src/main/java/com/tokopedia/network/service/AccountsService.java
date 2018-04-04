package com.tokopedia.network.service;

import android.os.Bundle;

import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.network.SessionUrl;

import retrofit2.Retrofit;

/**
 * @author stevenfredian on 5/25/16.
 */
public class AccountsService {

    public static final String ACCOUNTS = "ACCOUNTS";
    public static final String WS = "WS";

    public static final String AUTH_KEY = "AUTH_KEY";
    public static final String WEB_SERVICE = "WEB_SERVICE";
    public static final String USING_HMAC = "USING_HMAC";
    public static final String IS_BASIC = "IS_BASIC";
    public static final String USING_BOTH_AUTHORIZATION = "USING_BOTH_AUTHORIZATION";


    private static final String TAG = AccountsService.class.getSimpleName();
    protected AccountsApi api;

    public AccountsService(Bundle bundle) {

        String authKey = bundle.getString(AUTH_KEY, "");
        String webService = bundle.getString(WEB_SERVICE, ACCOUNTS);
        boolean isBasic = bundle.getBoolean(IS_BASIC, false);
        boolean isUsingHMAC = bundle.getBoolean(USING_HMAC, false);
        boolean isUsingBothAuthorization = bundle.getBoolean(USING_BOTH_AUTHORIZATION, false);

        initApiService(RetrofitFactory.createRetrofitDefaultConfig(getBaseUrl(webService))
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy())
                        .buildClientAccountsAuth(authKey,
                                isUsingHMAC,
                                isUsingBothAuthorization))
                .build());
    }

    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(AccountsApi.class);
    }

    protected String getBaseUrl(String webService) {
        switch (webService) {
            case WS:
                return SessionUrl.BASE_DOMAIN;
            case ACCOUNTS:
                return SessionUrl.ACCOUNTS_DOMAIN;
            default:
                throw new RuntimeException("unknown Base URL");
        }
    }

    public AccountsApi getApi() {
        return api;
    }

}
