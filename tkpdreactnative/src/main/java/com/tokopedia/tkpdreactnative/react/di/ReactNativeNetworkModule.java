package com.tokopedia.tkpdreactnative.react.di;

import android.content.Context;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpdreactnative.react.data.ReactNetworkRepositoryImpl;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkAuthDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkWsV4AuthDataSource;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkDefaultAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkFactory;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactDefaultAuthQualifier;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactDynamicAuthQualifier;
import com.tokopedia.tkpdreactnative.react.di.qualifier.ReactNoAuthQualifier;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkBearerDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkDataSource;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;
import com.tokopedia.tkpdreactnative.react.data.UnifyReactNetworkRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by alvarisi on 9/14/17.
 */
@Module
public class ReactNativeNetworkModule {

    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpNoAuth(OkHttpRetryPolicy okHttpRetryPolicy) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientNoAuth();
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpDefaultAuth(OkHttpRetryPolicy okHttpRetryPolicy) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDefaultAuth();
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    OkHttpClient provideOkhttpDynamicAuth(OkHttpRetryPolicy okHttpRetryPolicy) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDynamicAuth();
    }


    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitNoAuth(@ReactNoAuthQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitDefaultAuth(@ReactDefaultAuthQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    Retrofit provideRetrofitDynamicAuth(@ReactDynamicAuthQualifier OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.BASE_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @ReactNoAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonNoAuthService(@ReactNoAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @ReactDefaultAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonDefaultAuthService(@ReactDefaultAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @ReactDynamicAuthQualifier
    @Provides
    @ReactNativeNetworkScope
    CommonService provideCommonDynamicAuthService(@ReactDynamicAuthQualifier Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkFactory provdeReactNetworkFactory(@ReactNoAuthQualifier CommonService commonService) {
        return new ReactNetworkFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkDefaultAuthFactory provideReactNetworkDefaultAuthFactory(@ReactDefaultAuthQualifier CommonService commonService) {
        return new ReactNetworkDefaultAuthFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkAuthFactory provideReactNetworkAuthFactory(@ReactDynamicAuthQualifier CommonService commonService) {
        return new ReactNetworkAuthFactory(commonService);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkDataSource provideUnifyReactNetworkDataSource(Retrofit.Builder retrofitBuilder) {
        return new UnifyReactNetworkDataSource(retrofitBuilder);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkAuthDataSource provideUnifyReactNetworkAuthDataSource(Retrofit.Builder retrofitBuilder) {
        return new UnifyReactNetworkAuthDataSource(retrofitBuilder);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkWsV4AuthDataSource provideUnifyReactNetworkWsV4AuthDataSource(Retrofit.Builder retrofitBuilder,
                                                                               @ApplicationContext Context context) {
        return new UnifyReactNetworkWsV4AuthDataSource(retrofitBuilder, context);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkBearerDataSource provideUnifyReactNetworkBearerDataSource(Retrofit.Builder retrofitBuilder,
                                                                               SessionHandler sessionHandler) {
        return new UnifyReactNetworkBearerDataSource(retrofitBuilder, sessionHandler);
    }

    @Provides
    @ReactNativeNetworkScope
    UnifyReactNetworkRepository provideUnifyReactNetworkRepository(UnifyReactNetworkDataSource unifyReactNetworkDataSource,
                                                                   UnifyReactNetworkAuthDataSource unifyReactNetworkAuthDataSource,
                                                                   UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource,
                                                                   UnifyReactNetworkWsV4AuthDataSource unifyReactNetworkWsV4AuthDataSource) {
        return new UnifyReactNetworkRepositoryImpl(unifyReactNetworkDataSource, unifyReactNetworkAuthDataSource, unifyReactNetworkBearerDataSource, unifyReactNetworkWsV4AuthDataSource);
    }

    @Provides
    @ReactNativeNetworkScope
    ReactNetworkRepository provideReactNetworkRepository(@ApplicationContext Context context, ReactNetworkAuthFactory reactNetworkAuthFactory,
                                                         ReactNetworkFactory reactNetworkFactory,
                                                         ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory) {
        return new ReactNetworkRepositoryImpl(context, reactNetworkAuthFactory, reactNetworkFactory, reactNetworkDefaultAuthFactory);
    }
}
