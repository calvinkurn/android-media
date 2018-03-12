package com.tokopedia.flight.common.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;
import com.tokopedia.flight.banner.data.source.BannerDataSource;
import com.tokopedia.flight.booking.data.FlightPassengerFactorySource;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.booking.data.cloud.FlightSavedPassengerDataListCloudSource;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.FlightErrorResponse;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.data.source.FlightAuthInterceptor;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.BookingQualifier;
import com.tokopedia.flight.common.di.qualifier.FlightChuckQualifier;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderMapper;
import com.tokopedia.flight.review.data.FlightBookingDataSource;
import com.tokopedia.flight.review.data.FlightCheckVoucheCodeDataSource;
import com.tokopedia.flight.search.data.FlightSearchReturnDataSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataSource;
import com.tokopedia.flight.search.data.db.FlightMetaDataDBSource;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by User on 10/24/2017.
 */

@FlightScope
@Module
public class FlightModule {
    private static final int NET_READ_TIMEOUT = 30;
    private static final int NET_WRITE_TIMEOUT = 30;
    private static final int NET_CONNECT_TIMEOUT = 30;
    private static final int NET_RETRY = 1;

    @FlightScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }


    @FlightScope
    @Provides
    @FlightChuckQualifier
    public Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof FlightModuleRouter) {
            return ((FlightModuleRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + FlightModuleRouter.class.getSimpleName());
    }

    @FlightScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor,
                                            FlightAuthInterceptor flightAuthInterceptor,
                                            @FlightChuckQualifier Interceptor chuckIntereptor,
                                            @FlightQualifier OkHttpRetryPolicy okHttpRetryPolicy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout, TimeUnit.SECONDS)
                .addInterceptor(flightAuthInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(FlightErrorResponse.class));
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckIntereptor);
        }
        return builder.build();
    }

    @FlightScope
    @Provides
    @FlightQualifier
    public Retrofit provideFlightRetrofit(OkHttpClient okHttpClient,
                                          Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(FlightUrl.BASE_URL).client(okHttpClient).build();
    }

    @FlightScope
    @Provides
    public FlightRepository provideFlightRepository(BannerDataSource bannerDataSource,
                                                    FlightAirportDataListSource flightAirportDataListSource,
                                                    FlightAirlineDataListSource flightAirlineDataListSource,
                                                    FlightSearchSingleDataSource flightSearchSingleDataListSource,
                                                    FlightSearchReturnDataSource flightSearchReturnDataListSource,
                                                    FlightClassesDataSource getFlightClassesUseCase,
                                                    FlightCartDataSource flightCartDataSource,
                                                    FlightSavedPassengerDataListCloudSource flightSavedPassengerDataListCloudSource,
                                                    FlightMetaDataDBSource flightMetaDataDBSource,
                                                    FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                                    FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                                    FlightBookingDataSource flightBookingDataSource,
                                                    FlightAirportVersionDBSource flightAirportVersionDBSource,
                                                    FlightOrderDataSource flightOrderDataSource,
                                                    FlightOrderMapper flightOrderMapper,
                                                    FlightPassengerFactorySource flightPassengerFactorySource) {
        return new FlightRepositoryImpl(bannerDataSource, flightAirportDataListSource, flightAirlineDataListSource,
                flightSearchSingleDataListSource, flightSearchReturnDataListSource, getFlightClassesUseCase,
                flightCartDataSource, flightMetaDataDBSource, flightAirportDataListBackgroundSource,
                flightCheckVoucheCodeDataSource, flightBookingDataSource, flightAirportVersionDBSource,
                flightOrderDataSource, flightOrderMapper, flightPassengerFactorySource);
    }

    @Provides
    @BookingQualifier
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @FlightScope
    @Provides
    public FlightApi provideFlightAirportApi(@FlightQualifier Retrofit retrofit) {
        return retrofit.create(FlightApi.class);
    }

    @FlightScope
    @FlightQualifier
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY);
    }
}