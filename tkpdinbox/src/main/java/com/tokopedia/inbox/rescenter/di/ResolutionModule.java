package com.tokopedia.inbox.rescenter.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.rescenter.network.ResolutionApi;
import com.tokopedia.inbox.rescenter.network.ResolutionErrorInterceptor;
import com.tokopedia.inbox.rescenter.network.ResolutionErrorResponse;
import com.tokopedia.inbox.rescenter.network.ResolutionInterceptor;
import com.tokopedia.inbox.rescenter.network.ResolutionUrl;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.network.converter.TokopediaApiResponseConverter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by yfsx on 26/07/18.
 */
@Module
public class ResolutionModule {

    public static final String RESOLUTION_SERVICE = "Resolution_Service";
    public static final String RESOLUTION_IMAGE_SERVICE = "Resolution_Image_Service";

    @ResolutionScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

    @ResolutionScope
    @Provides
    public ResolutionInterceptor provideResolutionInterceptor(UserSession userSession) {
        return new ResolutionInterceptor(userSession);
    }

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
    @Named(RESOLUTION_SERVICE)
    public Retrofit provideResolutionServiceRetrofit(Retrofit.Builder retrofitBuilder,
                                              OkHttpClient okHttpClient,
                                              TokopediaApiResponseConverter tokopediaApiResponseConverter,
                                              StringResponseConverter stringResponseConverter) {
        return retrofitBuilder.baseUrl(ResolutionUrl.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(tokopediaApiResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .build();
    }

    @ResolutionScope
    @Provides
    @Named(RESOLUTION_IMAGE_SERVICE)
    public Retrofit provideResolutionImageServiceRetrofit(Retrofit.Builder retrofitBuilder,
                                              OkHttpClient okHttpClient,
                                              TokopediaApiResponseConverter tokopediaApiResponseConverter,
                                              StringResponseConverter stringResponseConverter) {
        return retrofitBuilder.baseUrl(ResolutionUrl.BASE_URL_IMAGE_SERVICE)
                .client(okHttpClient)
                .addConverterFactory(tokopediaApiResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .build();
    }

    @ResolutionScope
    @Provides
    public StringResponseConverter provideStringResponseConverter() {
        return new StringResponseConverter();
    }

    @ResolutionScope
    @Provides
    public TokopediaApiResponseConverter provideTokopediaApiResponseConverter() {
        return new TokopediaApiResponseConverter();
    }


    @ResolutionScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            ResolutionInterceptor resolutionInterceptor,
                                            @ApplicationContext Context context) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(ResolutionErrorResponse.class))
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context,
                        new com.tokopedia.user.session.UserSession(context)))
                .addInterceptor(resolutionInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @ResolutionScope
    @Provides
    @Named(RESOLUTION_SERVICE)
    public ResolutionApi provideResolutionApi(@Named(RESOLUTION_SERVICE) Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

    @ResolutionScope
    @Provides
    @Named(RESOLUTION_IMAGE_SERVICE)
    public ResolutionApi provideResolutionImageServiceApi(@Named(RESOLUTION_IMAGE_SERVICE) Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

}
