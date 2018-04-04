package com.tokopedia.network.service;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.network.SessionUrl;

import retrofit2.Retrofit;

/**
 * Created by nisie on 3/10/17.
 */

public class UploadImageService extends AuthService<UploadImageApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(UploadImageApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return SessionUrl.ACCOUNTS_DOMAIN;
    }

    @Override
    public UploadImageApi getApi() {
        return api;
    }
}
