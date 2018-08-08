package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 3/22/17.
 */

public class GenerateHostService extends AuthService<GenerateHostApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(GenerateHostApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Upload.URL_GENERATE_HOST_ACTION;
    }

    @Override
    public GenerateHostApi getApi() {
        return api;
    }
}
