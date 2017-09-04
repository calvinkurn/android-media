package com.tokopedia.ride.common.ride.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.ride.di.scope.RideScope;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;

import dagger.Component;

/**
 * Created by alvarisi on 7/24/17.
 */
@RideScope
@Component(modules = RideModule.class, dependencies = AppComponent.class)
public interface RideComponent {
    ApiCacheRepository apiCacheRepository();

    CacheApiWhiteListUseCase cacheApiWhiteListUseCase();

    ApiCacheDataSource cacheHelper();

    GCMHandler gcmHandler();

    SessionHandler sessionHandler();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    BookingRideRepository bookingRideRepository();

    PlaceRepository placeRepository();

    GetFareEstimateUseCase getFareEstimateUseCase();

    GetOverviewPolylineUseCase getOverviewPolylineUseCase();

    GiveDriverRatingUseCase giveDriverRatingUseCase();
}
