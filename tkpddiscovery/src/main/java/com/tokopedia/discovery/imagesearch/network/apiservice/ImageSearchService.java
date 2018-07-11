package com.tokopedia.discovery.imagesearch.network.apiservice;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.BaseService;
import com.tokopedia.discovery.imagesearch.network.apiservice.api.ImageSearchApi;

import retrofit2.Retrofit;

/**
 * Created by sachinbansal on 5/21/18.
 */

public class ImageSearchService extends BaseService<ImageSearchApi> {
    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(ImageSearchApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.HOME_DATA_BASE_URL;
    }

    @Override
    public ImageSearchApi getApi() {
        return api;
    }
}
