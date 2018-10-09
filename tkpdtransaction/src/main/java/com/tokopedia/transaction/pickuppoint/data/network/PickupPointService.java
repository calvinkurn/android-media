package com.tokopedia.transaction.pickuppoint.data.network;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Irfan Khoirul on 15/01/18.
 */

public class PickupPointService extends AuthService<PickupPointApi> {

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(PickupPointApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Transaction.URL_PICKUP_POINT;
    }

    @Override
    public PickupPointApi getApi() {
        return api;
    }

}
