package com.tokopedia.ride.bookingride.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.geolocation.domain.MapsRepository;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.ride.bookingride.di.scope.BookingRideScope;
import com.tokopedia.ride.bookingride.domain.AutoCompletePredictionUseCase;
import com.tokopedia.ride.bookingride.domain.GetCurrentRideRequestUseCase;
import com.tokopedia.ride.bookingride.domain.GetDistanceMatrixUseCase;
import com.tokopedia.ride.bookingride.domain.GetPlaceDetailUseCase;
import com.tokopedia.ride.bookingride.domain.GetPriceEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetProductAndEstimatedUseCase;
import com.tokopedia.ride.bookingride.domain.GetPromoUseCase;
import com.tokopedia.ride.bookingride.domain.GetUberProductsUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressCacheUseCase;
import com.tokopedia.ride.bookingride.domain.GetUserAddressUseCase;
import com.tokopedia.ride.common.place.domain.PlaceRepository;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 7/24/17.
 */
@Module
public class BookingRideModule {
    public BookingRideModule() {
    }

    @Provides
    @BookingRideScope
    GetCurrentRideRequestUseCase provideGetCurrentRideRequestUseCase(ThreadExecutor threadExecutor,
                                                                     PostExecutionThread postExecutionThread,
                                                                     BookingRideRepository bookingRideRepository) {
        return new GetCurrentRideRequestUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }


    @Provides
    @BookingRideScope
    GetUberProductsUseCase provideGetUberProductsUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         BookingRideRepository bookingRideRepository) {
        return new GetUberProductsUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetPromoUseCase provideGetPromoUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           BookingRideRepository bookingRideRepository) {
        return new GetPromoUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetUserAddressUseCase provideGetUserAddressUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       BookingRideRepository bookingRideRepository) {
        return new GetUserAddressUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetUserAddressCacheUseCase provideGetUserAddressCacheUseCase(ThreadExecutor threadExecutor,
                                                                 PostExecutionThread postExecutionThread,
                                                                 BookingRideRepository bookingRideRepository) {
        return new GetUserAddressCacheUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetProductAndEstimatedUseCase provideGetProductAndEstimatedUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       BookingRideRepository bookingRideRepository) {
        return new GetProductAndEstimatedUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetPriceEstimateUseCase provideGetPriceEstimateUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           BookingRideRepository bookingRideRepository) {
        return new GetPriceEstimateUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @BookingRideScope
    GetDistanceMatrixUseCase provideGetDistanceMatrixUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             PlaceRepository placeRepository) {
        return new GetDistanceMatrixUseCase(threadExecutor, postExecutionThread, placeRepository);
    }

    @Provides
    @BookingRideScope
    MapService provideMapService() {
        return new MapService();
    }

    @Provides
    @BookingRideScope
    MapsRepository provideMapsRepository() {
        return new MapsRepository();
    }

    @Provides
    @BookingRideScope
    AutoCompletePredictionUseCase provideAutoCompletePredictionUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       MapsRepository mapsRepository,
                                                                       MapService mapService) {
        return new AutoCompletePredictionUseCase(threadExecutor, postExecutionThread, mapsRepository, mapService);
    }

    @Provides
    @BookingRideScope
    GetPlaceDetailUseCase provideGetPlaceDetailUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       MapsRepository mapsRepository,
                                                       MapService mapService) {
        return new GetPlaceDetailUseCase(threadExecutor, postExecutionThread, mapsRepository, mapService);
    }


}