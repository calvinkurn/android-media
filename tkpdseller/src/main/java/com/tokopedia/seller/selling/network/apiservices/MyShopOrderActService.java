package com.tokopedia.seller.selling.network.apiservices;

import com.tokopedia.seller.selling.network.apiservices.apis.MyShopOrderActApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */
public class MyShopOrderActService extends AuthService<MyShopOrderActApi> {
    private static final String TAG = MyShopOrderActService.class.getSimpleName();

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
