package com.tokopedia.transaction.network;

import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.transaction.network.api.ProductChangeApi;

import retrofit2.Retrofit;

/**
 * Created by kris on 1/10/18. Tokopedia
 */

public class ProductChangeService extends AuthService<ProductChangeApi> {


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ProductChangeApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Product.URL_PRODUCT_ACTION;
    }

    @Override
    public ProductChangeApi getApi() {
        return api;
    }
}
