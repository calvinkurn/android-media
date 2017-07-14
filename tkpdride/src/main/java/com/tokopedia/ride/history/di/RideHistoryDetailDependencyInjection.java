package com.tokopedia.ride.history.di;

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
import com.tokopedia.ride.common.ride.data.BookingRideDataStoreFactory;
import com.tokopedia.ride.common.ride.data.BookingRideRepositoryData;
import com.tokopedia.ride.common.ride.data.ProductEntityMapper;
import com.tokopedia.ride.common.ride.data.TimeEstimateEntityMapper;
import com.tokopedia.ride.common.ride.data.source.api.RideApi;
import com.tokopedia.ride.common.ride.data.source.api.RideUrl;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.view.RideHistoryDetailContract;
import com.tokopedia.ride.history.view.RideHistoryDetailPresenter;

import retrofit2.Retrofit;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryDetailDependencyInjection {

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

    private GetSingleRideHistoryUseCase provideGetSingleRideHistoryUseCase(String token, String userId) {
        return new GetSingleRideHistoryUseCase(
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

    private GiveDriverRatingUseCase provideGiveDriverRatingUseCase(String token, String userId) {
        return new GiveDriverRatingUseCase(
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

    public static RideHistoryDetailContract.Presenter createPresenter(Context context) {
        SessionHandler sessionHandler = new SessionHandler(context);
        String token = String.format("Bearer %s", sessionHandler.getAccessToken(context));
        String userId = sessionHandler.getLoginID();
        RideHistoryDetailDependencyInjection injection = new RideHistoryDetailDependencyInjection();
        GetSingleRideHistoryUseCase getSingleRideHistoryUseCase = injection.provideGetSingleRideHistoryUseCase(token, userId);
        GiveDriverRatingUseCase giveDriverRatingUseCase = injection.provideGiveDriverRatingUseCase(token, userId);
        return new RideHistoryDetailPresenter(getSingleRideHistoryUseCase, giveDriverRatingUseCase);
    }
}
