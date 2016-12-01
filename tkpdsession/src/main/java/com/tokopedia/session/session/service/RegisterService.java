package com.tokopedia.session.session.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;
import com.tokopedia.session.session.api.RegisterApi;

import retrofit2.Retrofit;

/**
 * Created by m.normansyah on 1/25/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class RegisterService extends BaseService<RegisterApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(RegisterApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.User.URL_REGISTER;
    }

    @Override
    public RegisterApi getApi() {
        return api;
    }
}
