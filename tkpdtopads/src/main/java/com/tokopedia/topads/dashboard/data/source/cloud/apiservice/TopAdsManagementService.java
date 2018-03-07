package com.tokopedia.topads.dashboard.data.source.cloud.apiservice;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BearerService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;

import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class TopAdsManagementService extends BearerService<TopAdsManagementApi> {

    public TopAdsManagementService(SessionHandler sessionHandler) {
        super(sessionHandler.getAuthAccessToken());
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(TopAdsManagementApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.TOPADS_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return null;
    }

    @Override
    public TopAdsManagementApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientTopAdsAuth(null))
                .build();
    }
}