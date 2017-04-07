package com.tokopedia.ride.bookingride.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.ride.bookingride.data.PeopleAddressApi;
import com.tokopedia.ride.bookingride.data.PeopleAddressDataStoreFactory;
import com.tokopedia.ride.bookingride.data.PeopleAddressRepositoryData;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.PeopleAddressRepository;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompleteContract;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompletePresenter;
import com.tokopedia.ride.common.ride.data.source.api.RideUrl;

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

    public OkHttpClient provideOkhttpClient(OkHttpRetryPolicy okHttpRetryPolicy) {
        return OkHttpFactory.create()
                .addOkHttpRetryPolicy(okHttpRetryPolicy)
                .buildClientDefaultAuth();
    }

    private Retrofit provideRideRetrofit(OkHttpClient client,
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

    private PeopleAddressApi provideRideApi(Retrofit retrofit) {
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

    private PeopleAddressDataStoreFactory provideBookingRideDataStoreFactory(PeopleAddressApi peopleAddressApi) {
        return new PeopleAddressDataStoreFactory(peopleAddressApi);
    }

    private GetPeopleAddressesUseCase provideGetFareEstimateUseCase() {
        return new GetPeopleAddressesUseCase(
                provideThreadExecutor(),
                providePostExecutionThread(),
                providePeopleAddressRepository(
                        provideBookingRideDataStoreFactory(
                                provideRideApi(
                                        provideRideRetrofit(
                                                provideOkhttpClient(provideOkHttpRetryPolicy()),
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

    public static PlaceAutoCompleteContract.Presenter createPresenter(){
        PlaceAutoCompleteDependencyInjection injection = new PlaceAutoCompleteDependencyInjection();

        GetPeopleAddressesUseCase getPeopleAddressesUseCase = injection.provideGetFareEstimateUseCase();
        return new PlaceAutoCompletePresenter(getPeopleAddressesUseCase);
    }
}
