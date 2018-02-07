package com.tokopedia.home.beranda.data.source.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.home.common.HomeDataApi;

import retrofit2.Retrofit;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class HomeDataService extends AuthService<HomeDataApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(HomeDataApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public HomeDataApi getApi() {
        return api;
    }
}
