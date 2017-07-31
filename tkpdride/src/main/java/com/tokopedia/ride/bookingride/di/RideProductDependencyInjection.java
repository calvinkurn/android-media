package com.tokopedia.ride.bookingride.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.core.OkHttpRetryPolicy;
import com.tokopedia.core.network.core.RetrofitFactory;
import com.tokopedia.core.network.retrofit.interceptors.DebugInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.RideInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetPriceEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetProductAndEstimatedUseCase;
import com.tokopedia.ride.bookingride.domain.GetTimePriceEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.UberProductPresenter;
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.ProductEntityMapper;
import com.tokopedia.ride.common.ride.data.TimeEstimateEntityMapper;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.data.source.api.RideUrl;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import retrofit2.Retrofit;

/**
 * Created by alvarisi on 3/16/17.
 */

public class RideProductDependencyInjection {

    private ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    private PostExecutionThread providePostExecutionThread() {
        return new UIThread();
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


    private GetProductAndEstimatedUseCase provideGetProductAndEstimatedUseCase(String token, String userId) {
        return new GetProductAndEstimatedUseCase(
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

    private GetFareEstimateUseCase provideGetFareEstimateUseCase(String token, String userId) {
        return new GetFareEstimateUseCase(
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

    private GetTimePriceEstimateUseCase provideGetTimePriceEstimate(String token, String userId) {
        return new GetTimePriceEstimateUseCase(
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

    private GetUberProductsUseCase provideGetUberProductsUseCase(String token, String userId) {
        return new GetUberProductsUseCase(
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

    private GetPriceEstimateUseCase provideGetPriceEstimateUseCase(String token, String userId) {
        return new GetPriceEstimateUseCase(
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


    public static UberProductContract.Presenter createPresenter(Context context) {
        SessionHandler sessionHandler = new SessionHandler(context);
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(context));
        String userId = sessionHandler.getLoginID();
        RideProductDependencyInjection injection = new RideProductDependencyInjection();
        GetProductAndEstimatedUseCase getProductAndEstimatedUseCase = injection.provideGetProductAndEstimatedUseCase(token, userId);
        GetPriceEstimateUseCase getPriceEstimateUseCase = injection.provideGetPriceEstimateUseCase(token, userId);
        return new UberProductPresenter(getProductAndEstimatedUseCase, getPriceEstimateUseCase);
    }
}
