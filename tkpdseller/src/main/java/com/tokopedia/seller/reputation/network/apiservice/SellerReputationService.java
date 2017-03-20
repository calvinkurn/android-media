package com.tokopedia.seller.reputation.network.apiservice;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.reputation.constant.ReputationConstant;
import com.tokopedia.seller.reputation.network.apiservice.api.SellerReputationApi;

import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 3/20/17.
 */

public class SellerReputationService extends AuthService<SellerReputationApi> {
    private static final String TAG = "SellerReputationService";

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(SellerReputationApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return ReputationConstant.baseUrl;
    }

    @Override
    public SellerReputationApi getApi() {
        return api;
    }
}
