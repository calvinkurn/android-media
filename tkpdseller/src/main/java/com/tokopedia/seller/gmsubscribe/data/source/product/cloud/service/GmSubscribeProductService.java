package com.tokopedia.seller.gmsubscribe.data.source.product.cloud.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;

import retrofit2.Retrofit;

/**
 * Created by sebastianuskh on 3/13/17.
 */

public class GmSubscribeProductService extends AuthService<GoldMerchantApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GoldMerchantApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.GOLD_MERCHANT_DOMAIN;
    }

    @Override
    public GoldMerchantApi getApi() {
        return api;
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        return RetrofitFactory.createRetrofitDefaultConfig(processedBaseUrl)
                .client(OkHttpFactory.create()
                        .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                        .buildClientBearerWithClientDefaultAuth())
                .build();
    }
}
