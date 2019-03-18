package com.tokopedia.inbox.rescenter.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;
import com.tokopedia.inbox.rescenter.network.ResolutionErrorInterceptor;
import com.tokopedia.inbox.rescenter.network.ResolutionErrorResponse;
import com.tokopedia.inbox.rescenter.network.ResolutionUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by yfsx on 26/07/18.
 */
@Module
public class ResolutionModule {

    public static final String RESOLUTION_SERVICE = "Resolution_Service";

    @ResolutionScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }

    @ResolutionScope
    @Provides
    public ResolutionErrorInterceptor provideResoErrorInterceptor() {
        return new ResolutionErrorInterceptor(ResolutionErrorResponse.class);
    }

    @ResolutionScope
    @Provides
    public Retrofit provideResolutionServiceRetrofit(Retrofit.Builder retrofitBuilder,
                                                     OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(ResolutionUrl.BASE_URL)
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @ResolutionScope
    @Provides
    public TkpdAuthInterceptor providesTkpdAuthInterceptor(@ApplicationContext Context context) {
        return new TkpdAuthInterceptor(context,
                (NetworkRouter) context,
                new com.tokopedia.user.session.UserSession(context));
    }

    @ResolutionScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @ApplicationContext Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(ResolutionErrorResponse.class))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context,
                        new com.tokopedia.user.session.UserSession(context)))
                .addInterceptor(tkpdAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @ResolutionScope
    @Provides
    public ResolutionApi provideResolutionApi(Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }


}
