package com.tokopedia.transaction.network;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.TkpdOkHttpBuilder;
import com.tokopedia.core.network.retrofit.interceptors.FingerprintInterceptor;
import com.tokopedia.core.network.retrofit.services.AuthService;
import com.tokopedia.loyalty.domain.apiservice.TokoPointAuthInterceptor;
import com.tokopedia.loyalty.domain.apiservice.TokoPointRetrofitFactory;
import com.tokopedia.transaction.network.api.VoucherCartApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by kris on 3/29/18. Tokopedia
 */

public class VoucherCartService extends AuthService<VoucherCartApi> {

    private static final String TAG = VoucherCartService.class.getSimpleName();

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(VoucherCartApi.class);
    }

    @Override
    protected Retrofit createRetrofitInstance(String processedBaseUrl) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = OkHttpFactory.create()
                .addOkHttpRetryPolicy(getOkHttpRetryPolicy())
                .getClientBuilder();
        TkpdOkHttpBuilder tkpdOkHttpBuilder = new TkpdOkHttpBuilder(builder);
        // tkpdOkHttpBuilder.addInterceptor(loggingInterceptor);
        tkpdOkHttpBuilder.addInterceptor(new FingerprintInterceptor());
        tkpdOkHttpBuilder.setOkHttpRetryPolicy(getOkHttpRetryPolicy());
        tkpdOkHttpBuilder.addDebugInterceptor();
        OkHttpClient okHttpClient = tkpdOkHttpBuilder.build();

        return TokoPointRetrofitFactory.createRetrofitTokoPointConfig(processedBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Override
    protected String getBaseUrl() {
        return TransactionUrl.CART_PROMO;
    }

    @Override
    public VoucherCartApi getApi() {
        return api;
    }

}
