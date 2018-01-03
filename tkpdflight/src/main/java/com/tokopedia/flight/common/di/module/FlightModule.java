package com.tokopedia.flight.common.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListBackgroundSource;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;
import com.tokopedia.flight.banner.data.source.BannerDataSource;
import com.tokopedia.flight.booking.data.cloud.FlightCartDataSource;
import com.tokopedia.flight.common.di.qualifier.BookingQualifier;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.model.FlightErrorResponse;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.data.source.FlightAuthInterceptor;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
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

    @FlightScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    @FlightScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context,  OkHttpClient.Builder okHttpClientBuilder,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            FlightAuthInterceptor flightAuthInterceptor) {
        return okHttpClientBuilder
                .addInterceptor(flightAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(FlightErrorResponse.class))
                .build();
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
                                                    FlightMetaDataDBSource flightMetaDataDBSource,
                                                    FlightAirportDataListBackgroundSource flightAirportDataListBackgroundSource,
                                                    FlightCheckVoucheCodeDataSource flightCheckVoucheCodeDataSource,
                                                    FlightBookingDataSource flightBookingDataSource,
                                                    FlightAirportVersionDBSource flightAirportVersionDBSource,
                                                    FlightOrderDataSource flightOrderDataSource,
                                                    FlightOrderMapper flightOrderMapper) {
        return new FlightRepositoryImpl(bannerDataSource, flightAirportDataListSource,flightAirlineDataListSource,
                flightSearchSingleDataListSource, flightSearchReturnDataListSource, getFlightClassesUseCase, flightCartDataSource,
                flightMetaDataDBSource, flightAirportDataListBackgroundSource, flightCheckVoucheCodeDataSource, flightBookingDataSource,
                flightAirportVersionDBSource, flightOrderDataSource, flightOrderMapper);
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
}