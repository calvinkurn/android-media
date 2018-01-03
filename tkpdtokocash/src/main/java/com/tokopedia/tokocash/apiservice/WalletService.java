package com.tokopedia.tokocash.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.BearerService;

import retrofit2.Retrofit;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public class WalletService extends BearerService<WalletApi> {

    public WalletService(String mToken) {
        super(mToken);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.WALLET_DOMAIN;
    }

    @Override
    protected String getOauthAuthorization() {
        return "Bearer " + mToken;
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        this.mApi = retrofit.create(WalletApi.class);
    }

    @Override
    public WalletApi getApi() {
        Retrofit retrofit = RetrofitFactory.createRetrofitTokoCashConfig(getBaseUrl())
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerWalletAuth(getOauthAuthorization()))
                .build();
        initApiService(retrofit);
        return this.mApi;
    }
}
