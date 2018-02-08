package com.tokopedia.transaction.insurance.data.network;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by Irfan Khoirul on 12/12/17.
 */

public class InsuranceWebViewService extends AuthService<InsuranceApi> {

    public InsuranceWebViewService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(InsuranceApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_SHIPPING_WEBVIEW;
    }

    @Override
    public InsuranceApi getApi() {
        return api;
    }
}
