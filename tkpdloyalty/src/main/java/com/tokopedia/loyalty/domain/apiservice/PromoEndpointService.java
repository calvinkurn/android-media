package com.tokopedia.loyalty.domain.apiservice;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public class PromoEndpointService extends BaseService<PromoApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PromoApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.PROMO_API_DOMAIN;
    }

    @Override
    public PromoApi getApi() {
        return this.api;
    }
}
