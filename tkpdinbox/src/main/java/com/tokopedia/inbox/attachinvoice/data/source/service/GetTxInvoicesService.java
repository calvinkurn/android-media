package com.tokopedia.inbox.attachinvoice.data.source.service;

import com.google.gson.Gson;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.inbox.attachinvoice.data.source.api.GetTxInvoicesApi;
import com.tokopedia.inbox.attachproduct.data.source.api.TomeGetShopProductAPI;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hendri on 21/03/18.
 */

public class GetTxInvoicesService extends AuthService<GetTxInvoicesApi>{
    public static final String CS_CHATBOT_PATH = "/cs/chatbot/";

    @Override
    protected void initApiService(Retrofit retrofit) {
            api = retrofit.create(GetTxInvoicesApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.BASE_API_DOMAIN+CS_CHATBOT_PATH;
    }

    @Override
    public GetTxInvoicesApi getApi() {
        return api;
    }

}
