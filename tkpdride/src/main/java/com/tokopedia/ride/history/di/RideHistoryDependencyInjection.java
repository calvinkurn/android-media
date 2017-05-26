package com.tokopedia.ride.history.di;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.network.RideInterceptor;
import com.tokopedia.ride.common.place.data.DirectionEntityMapper;
import com.tokopedia.ride.common.place.data.PlaceDataRepository;
import com.tokopedia.ride.common.place.data.PlaceDataStoreFactory;
import com.tokopedia.ride.common.place.data.source.api.PlaceApi;
import com.tokopedia.ride.common.place.data.source.api.PlaceUrl;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.ProductEntityMapper;
import com.tokopedia.ride.common.ride.data.TimeEstimateEntityMapper;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.data.source.api.RideUrl;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.view.RideHistoryContract;
import com.tokopedia.ride.history.view.RideHistoryPresenter;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alvarisi on 4/19/17.
 */

public class RideHistoryDependencyInjection {
    private Gson provideGson() {
        return new Gson();
    }

    private ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    private PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    private GeneratedHostConverter provideGeneratedHostConverter() {
        return new GeneratedHostConverter();
    }

    private TkpdResponseConverter provideTkpdResponseConverter() {
        return new TkpdResponseConverter();
    }

    private StringResponseConverter provideResponseConverter() {
        return new StringResponseConverter();
    }

    private GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    private RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    private RideInterceptor provideRideInterceptor(String token, String userId) {
        return new RideInterceptor(token, userId);
    }

    private HttpLoggingInterceptor provideLoggingInterceptory() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    private ChuckInterceptor provideChuckInterceptor() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
        return new ChuckInterceptor(MainApplication.getAppContext())
                .showNotification(localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false));
    }

    private OkHttpClient provideRideOkHttpClient(RideInterceptor rideInterceptor, HttpLoggingInterceptor loggingInterceptor, ChuckInterceptor chuckInterceptor) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.interceptors().add(rideInterceptor);
        clientBuilder.interceptors().add(loggingInterceptor);
        clientBuilder.interceptors().add(chuckInterceptor);
        return clientBuilder.build();
    }

    private Retrofit provideRideRetrofit(OkHttpClient client,
                                         GeneratedHostConverter hostConverter,
                                         TkpdResponseConverter tkpdResponseConverter,
                                         StringResponseConverter stringResponseConverter,
                                         GsonConverterFactory gsonConverterFactory,
                                         RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return createRetrofit(RideUrl.BASE_URL,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
    }

    private RideApi provideRideApi(Retrofit retrofit) {
        return retrofit.create(RideApi.class);
    }

    private BookingRideDataStoreFactory provideBookingRideDataStoreFactory(RideApi rideApi) {
        return new BookingRideDataStoreFactory(rideApi);
    }

    private BookingRideRepository provideBookingRideRepository(BookingRideDataStoreFactory factory,
                                                               ProductEntityMapper mapper, TimeEstimateEntityMapper estimateEntityMapper) {
        return new BookingRideRepositoryData(factory, mapper, estimateEntityMapper);
    }

    private static Retrofit createRetrofit(String baseUrl,
                                           OkHttpClient client,
                                           GeneratedHostConverter hostConverter,
                                           TkpdResponseConverter tkpdResponseConverter,
                                           StringResponseConverter stringResponseConverter,
                                           GsonConverterFactory gsonConverterFactory,
                                           RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(hostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

    public static RideHistoryContract.Presenter createPresenter(Context context) {
        SessionHandler sessionHandler = new SessionHandler(context);
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(context));
        String userId = sessionHandler.getLoginID();
        RideHistoryDependencyInjection injection = new RideHistoryDependencyInjection();
        GetRideHistoriesUseCase getRideHistoriesUseCase = injection.provideGetRideHistoriesUseCase(token, userId);
        GetOverviewPolylineUseCase getOverviewPolylineUseCase = injection.getOverviewPolylineUseCase(context);
        return new RideHistoryPresenter(getRideHistoriesUseCase, getOverviewPolylineUseCase);
    }

    private GetRideHistoriesUseCase provideGetRideHistoriesUseCase(String token, String userId) {
        return new GetRideHistoriesUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                provideBookingRideRepository(
                        provideBookingRideDataStoreFactory(
                                provideRideApi(
                                        provideRideRetrofit(
                                                provideRideOkHttpClient(provideRideInterceptor(token, userId),
                                                        provideLoggingInterceptory(),
                                                        provideChuckInterceptor()),
                                                provideGeneratedHostConverter(),
                                                provideTkpdResponseConverter(),
                                                provideResponseConverter(),
                                                provideGsonConverterFactory(provideGson()),
                                                provideRxJavaCallAdapterFactory()
                                        )
                                )
                        ),
                        new ProductEntityMapper(),
                        new TimeEstimateEntityMapper()
                )
        );
    }


    private OkHttpClient providePlaceOkHttpClient(Cache cache, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cache(cache);
        return client.build();
    }

    private Retrofit providePlaceRetrofit(OkHttpClient client,
                                          GeneratedHostConverter hostConverter,
                                          TkpdResponseConverter tkpdResponseConverter,
                                          StringResponseConverter stringResponseConverter,
                                          GsonConverterFactory gsonConverterFactory,
                                          RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return createRetrofit(PlaceUrl.BASE_URL,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
    }

    private PlaceApi providePlaceApi(Retrofit retrofit) {
        return retrofit.create(PlaceApi.class);
    }

    private PlaceDataStoreFactory providePlaceDataStoreFactory(PlaceApi placeApi) {
        return new PlaceDataStoreFactory(placeApi);
    }

    private PlaceRepository providePlaceRepository(PlaceDataStoreFactory placeDataStoreFactory, DirectionEntityMapper mapper) {
        return new PlaceDataRepository(placeDataStoreFactory, mapper);
    }

    private Cache provideHttpCacheCache(Context context) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(context.getCacheDir(), cacheSize);
    }

    private GetOverviewPolylineUseCase getOverviewPolylineUseCase(Context context) {
        return new GetOverviewPolylineUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                providePlaceRepository(providePlaceDataStoreFactory(providePlaceApi(providePlaceRetrofit(
                        providePlaceOkHttpClient(provideHttpCacheCache(context),
                                provideLoggingInterceptory()),
                        provideGeneratedHostConverter(),
                        provideTkpdResponseConverter(),
                        provideResponseConverter(),
                        provideGsonConverterFactory(provideGson()),
                        provideRxJavaCallAdapterFactory()
                ))), new DirectionEntityMapper())
        );
    }
}
