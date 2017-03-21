package com.tokopedia.ride.bookingride.di;

import com.google.gson.Gson;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.ProductEntityMapper;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.data.source.api.RideUrl;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.UberProductPresenter;
import com.tokopedia.ride.common.network.RideInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductDependencyInjection {
    public static UberProductContract.Presenter createPresenter(String token) {
        Gson gson = new Gson();

        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        GeneratedHostConverter hostConverter = new GeneratedHostConverter();
        TkpdResponseConverter tkpdResponseConverter = new TkpdResponseConverter();
        StringResponseConverter stringResponseConverter = new StringResponseConverter();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);

        RideInterceptor rideInterceptor = new RideInterceptor(token);
        clientBuilder.interceptors().add(rideInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client = clientBuilder.build();


        Retrofit retrofit = createRetrofit(RideUrl.BASE_URL,
                client,
                hostConverter,
                tkpdResponseConverter,
                stringResponseConverter,
                gsonConverterFactory,
                rxJavaCallAdapterFactory
        );


        RideApi uberApi = retrofit.create(RideApi.class);
        BookingRideDataStoreFactory bookingRideDataStoreFactory = new BookingRideDataStoreFactory(uberApi);
        BookingRideRepository repository = new BookingRideRepositoryData(bookingRideDataStoreFactory
                , new ProductEntityMapper(), mTimeEstimateEntityMapper);

        GetUberProductsUseCase getUberProductsUseCase = new GetUberProductsUseCase(
                threadExecutor,
                postExecutionThread,
                repository
        );
        return new UberProductPresenter(getUberProductsUseCase);
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
}
