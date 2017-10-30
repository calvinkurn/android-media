package com.tokopedia.flight.common.di.module;

import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.data.FlightSearchReturnDataListSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataListSource;

import dagger.Module;
import dagger.Provides;
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
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder okHttpClientBuilder, HttpLoggingInterceptor httpLoggingInterceptor) {
        return okHttpClientBuilder.addInterceptor(httpLoggingInterceptor).build();
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
    public FlightRepository provideFlightRepository(FlightAirportDataListSource flightAirportDataListSource,
                                                    FlightSearchSingleDataListSource flightSearchSingleDataListSource,
                                                    FlightSearchReturnDataListSource flightSearchReturnDataListSource){
        return new FlightRepositoryImpl(flightAirportDataListSource, flightSearchSingleDataListSource, flightSearchReturnDataListSource);
    }

    @FlightScope
    @Provides
    public FlightApi provideFlightAirportApi(@FlightQualifier Retrofit retrofit){
        return retrofit.create(FlightApi.class);
    }
}