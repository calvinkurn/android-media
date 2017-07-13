package com.tokopedia.ride.bookingride.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.RideInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.bookingride.data.PeopleAddressApi;
import com.tokopedia.ride.bookingride.data.PeopleAddressDataStoreFactory;
import com.tokopedia.ride.bookingride.data.PeopleAddressRepositoryData;
import com.tokopedia.ride.bookingride.domain.GetDistanceMatrixUseCase;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressUseCase;
import com.tokopedia.ride.bookingride.domain.PeopleAddressRepository;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompleteContract;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompletePresenter;
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

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alvarisi on 4/6/17.
 */

public class PlaceAutoCompleteDependencyInjection {

    public PlaceAutoCompleteDependencyInjection() {
    }

    private Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
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

    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
    }

    private HttpLoggingInterceptor provideLoggingInterceptory() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logInterceptor;
    }

    private RideInterceptor provideRideInterceptor(String token, String userId) {
        return new RideInterceptor(token, userId);
    }

    private ChuckInterceptor provideChuckInterceptor() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), DeveloperOptions.CHUCK_ENABLED);
        return new ChuckInterceptor(MainApplication.getAppContext())
                .showNotification(localCacheHandler.getBoolean(DeveloperOptions.IS_CHUCK_ENABLED, false));
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

    public OkHttpClient provideOkhttpClient(OkHttpRetryPolicy okHttpRetryPolicy, ChuckInterceptor chuckInterceptor) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDefaultAuth();
    }

    private Retrofit provideMarketPlaceRetrofit(OkHttpClient client,
                                                GeneratedHostConverter hostConverter,
                                                TkpdResponseConverter tkpdResponseConverter,
                                                StringResponseConverter stringResponseConverter,
                                                GsonConverterFactory gsonConverterFactory,
                                                RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return createRetrofit(TkpdBaseURL.STAGE_DOMAIN,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory);
    }

    private PeopleAddressApi providePeopleAddressApi(Retrofit retrofit) {
        return retrofit.create(PeopleAddressApi.class);
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

    private PeopleAddressRepository providePeopleAddressRepository(PeopleAddressDataStoreFactory peopleAddressDataStore) {
        return new PeopleAddressRepositoryData(peopleAddressDataStore);
    }

    private PeopleAddressDataStoreFactory providePeopleAddressDataStoreFactory(PeopleAddressApi peopleAddressApi) {
        return new PeopleAddressDataStoreFactory(peopleAddressApi);
    }


    private GetPeopleAddressesUseCase provideGetFareEstimateUseCase() {
        return new GetPeopleAddressesUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                providePeopleAddressRepository(
                        providePeopleAddressDataStoreFactory(
                                providePeopleAddressApi(
                                        provideMarketPlaceRetrofit(
                                                provideOkhttpClient(provideOkHttpRetryPolicy(),
                                                        provideChuckInterceptor()),
                                                provideGeneratedHostConverter(),
                                                provideTkpdResponseConverter(),
                                                provideResponseConverter(),
                                                provideGsonConverterFactory(provideGson()),
                                                provideRxJavaCallAdapterFactory()
                                        )
                                )
                        )
                )
        );
    }

    private GetUserAddressCacheUseCase provideGetUserAddressCacheUseCase(String token, String userId) {
        return new GetUserAddressCacheUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                provideBookingRideRepository(
                        provideBookingRideDataStoreFactory(
                                provideRideApi(
                                        RetrofitFactory.createRetrofitDefaultConfig(RideUrl.BASE_URL)
                                                .client(OkHttpFactory.create().buildDaggerClientBearerRidehailing(provideRideInterceptor(token, userId),
                                                        OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy(),
                                                        provideChuckInterceptor(),
                                                        new DebugInterceptor()
                                                        )
                                                )
                                                .build()
                                )
                        ),
                        new ProductEntityMapper(),
                        new TimeEstimateEntityMapper()
                )
        );
    }

    public static PlaceAutoCompleteContract.Presenter createPresenter(Context context) {
        SessionHandler sessionHandler = new SessionHandler(context);
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(context));
        String userId = sessionHandler.getLoginID();
        PlaceAutoCompleteDependencyInjection injection = new PlaceAutoCompleteDependencyInjection();

        GetPeopleAddressesUseCase getPeopleAddressesUseCase = injection.provideGetFareEstimateUseCase();
        GetUserAddressUseCase getUserAddressUseCase = injection.provideGetUserAddressUseCase(token, userId);
        GetUserAddressCacheUseCase getUserAddressCacheUseCase = injection.provideGetUserAddressCacheUseCase(token, userId);
        GetDistanceMatrixUseCase getDistanceMatrixUseCase = injection.provideDistanceMatrixUseCase(context);
        return new PlaceAutoCompletePresenter(getPeopleAddressesUseCase, getUserAddressUseCase, getUserAddressCacheUseCase, getDistanceMatrixUseCase);
    }

    private GetUserAddressUseCase provideGetUserAddressUseCase(String token, String userId) {
        return new GetUserAddressUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                provideBookingRideRepository(
                        provideBookingRideDataStoreFactory(
                                provideRideApi(
                                        RetrofitFactory.createRetrofitDefaultConfig(RideUrl.BASE_URL)
                                                .client(OkHttpFactory.create().buildDaggerClientBearerRidehailing(provideRideInterceptor(token, userId),
                                                        OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy(),
                                                        provideChuckInterceptor(),
                                                        new DebugInterceptor()
                                                        )
                                                )
                                                .build()
                                )
                        ),
                        new ProductEntityMapper(),
                        new TimeEstimateEntityMapper()
                )
        );
    }

    private GetDistanceMatrixUseCase provideDistanceMatrixUseCase(Context context) {
        return new GetDistanceMatrixUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                providePlaceRepository(providePlaceDataStoreFactory(providePlaceApi(providePlaceRetrofit(
                        providePlaceOkHttpClient(provideHttpCacheCache(context),
                                provideLoggingInterceptory(),
                                provideChuckInterceptor()),
                        provideGeneratedHostConverter(),
                        provideTkpdResponseConverter(),
                        provideResponseConverter(),
                        provideGsonConverterFactory(provideGson()),
                        provideRxJavaCallAdapterFactory()
                ))), new DirectionEntityMapper())
        );
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

    private OkHttpClient providePlaceOkHttpClient(Cache cache, HttpLoggingInterceptor loggingInterceptor, ChuckInterceptor chuckInterceptor) {
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

    private Cache provideHttpCacheCache(Context context) {
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(context.getCacheDir(), cacheSize);
    }
}
