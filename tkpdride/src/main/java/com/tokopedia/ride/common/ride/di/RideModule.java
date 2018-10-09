package com.tokopedia.ride.common.ride.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.RideInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetLocationAddressUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.place.data.PlaceDataRepository;
import com.tokopedia.ride.common.place.data.PlaceDataStoreFactory;
import com.tokopedia.ride.common.place.data.source.api.PlaceApi;
import com.tokopedia.ride.common.place.data.source.api.PlaceUrl;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.di.scope.RideScope;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by alvarisi on 7/24/17.
 */
@Module
public class RideModule {
    private static final int CACHE_SIZE = 10485760;

    public RideModule() {
    }

    @Provides
    @RideScope
    RideApi provideRideApi(@RideQualifier Retrofit retrofit) {
        return retrofit.create(RideApi.class);
    }


    @Provides
    @RideScope
    BookingRideDataStoreFactory provideBookingRideDataStoreFactory(RideApi rideApi) {
        return new BookingRideDataStoreFactory(rideApi);
    }

    @Provides
    @RideScope
    BookingRideRepository provideBookingRideRepository(BookingRideDataStoreFactory bookingRideDataStoreFactory) {
        return new BookingRideRepositoryData(bookingRideDataStoreFactory);
    }


    @Provides
    @RideQualifier
    @RideScope
    Retrofit provideRideRetrofit(@RideQualifier OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TkpdBaseURL.RIDE_DOMAIN).client(okHttpClient).build();
    }

    @Provides
    @RideScope
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingLevel = HttpLoggingInterceptor.Level.BODY;
        }
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    @RideQualifier
    @Provides
    @RideScope
    OkHttpClient provideOkHttpClientRide(RideInterceptor rideInterceptor,
                                         OkHttpRetryPolicy okHttpRetryPolicy,
                                         ChuckInterceptor chuckInterceptor,
                                         DebugInterceptor debugInterceptor,
                                         HttpLoggingInterceptor loggingInterceptor) {

        return OkHttpFactory.create().buildDaggerClientBearerRidehailing(
                rideInterceptor,
                okHttpRetryPolicy,
                chuckInterceptor,
                debugInterceptor,
                loggingInterceptor
        );
    }

    @Provides
    @RideScope
    RideInterceptor provideRideInterCeptor() {
        String oAuthString = "Bearer " + SessionHandler.getAccessToken();
        return new RideInterceptor(oAuthString);
    }

    @RideScope
    @Provides
    @GoogleMapQualifier
    Retrofit provideGoogleMapRetrofit(@GoogleMapQualifier OkHttpClient okHttpClient,
                                      Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(PlaceUrl.BASE_URL).client(okHttpClient).build();
    }


    @RideScope
    @Provides
    @GoogleMapQualifier
    PlaceApi providePlaceApi(@GoogleMapQualifier Retrofit retrofit) {
        return retrofit.create(PlaceApi.class);
    }

    @RideScope
    @Provides
    @GoogleMapQualifier
    PlaceDataStoreFactory providePlaceDataStoreFactory(@GoogleMapQualifier PlaceApi placeApi) {
        return new PlaceDataStoreFactory(placeApi);
    }

    @RideScope
    @GoogleMapQualifier
    @Provides
    OkHttpClient providePlaceOkHttpClient(@GoogleMapQualifier Cache cache, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache);
        return client.build();
    }

    @RideScope
    @GoogleMapQualifier
    @Provides
    Cache provideHttpCacheCache(@ApplicationContext Context context) {
        return new Cache(context.getCacheDir(), CACHE_SIZE);
    }

    @RideScope
    @Provides
    PlaceRepository providePlaceRepository(@GoogleMapQualifier PlaceDataStoreFactory placeDataStoreFactory) {
        return new PlaceDataRepository(placeDataStoreFactory);
    }

    @Provides
    @RideScope
    GetFareEstimateUseCase provideGetSingleRideHistoryUseCase(ThreadExecutor threadExecutor,
                                                              PostExecutionThread postExecutionThread,
                                                              BookingRideRepository bookingRideRepository) {
        return new GetFareEstimateUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @RideScope
    GiveDriverRatingUseCase provideGiveDriverRatingUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           BookingRideRepository bookingRideRepository) {
        return new GiveDriverRatingUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }


    @Provides
    @RideScope
    GetOverviewPolylineUseCase proGetOverviewPolylineUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             PlaceRepository placeRepository) {
        return new GetOverviewPolylineUseCase(threadExecutor, postExecutionThread, placeRepository);
    }

    @Provides
    @RideScope
    GetLocationAddressUseCase provideGetLocationAddressUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               PlaceRepository placeRepository) {
        return new GetLocationAddressUseCase(threadExecutor, postExecutionThread, placeRepository);
    }

}
