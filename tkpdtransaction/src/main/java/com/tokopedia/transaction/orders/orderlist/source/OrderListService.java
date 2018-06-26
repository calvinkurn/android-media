package com.tokopedia.transaction.orders.orderlist.source;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;

import retrofit2.Retrofit;

/**
 * Created by baghira on 19/04/18.
 */

public class OrderListService extends AuthService<OrderListDataApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(OrderListDataApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public OrderListDataApi getApi() {
        return api;
    }
}