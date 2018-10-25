package com.tokopedia.transaction.network;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.transaction.network.api.MyShopOrderActApi;

import retrofit2.Retrofit;

/**
 * Created by kris on 12/28/17. Tokopedia
 */

public class MyShopOrderActService extends AuthService<MyShopOrderActApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(MyShopOrderActApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_ORDER_ACTION;
    }

    @Override
    public MyShopOrderActApi getApi() {
        return api;
    }
}
