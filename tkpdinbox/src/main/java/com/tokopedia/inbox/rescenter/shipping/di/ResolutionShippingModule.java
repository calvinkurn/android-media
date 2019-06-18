package com.tokopedia.inbox.rescenter.shipping.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.inbox.rescenter.di.ResolutionScope;
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
class ResolutionShippingModule {


}
