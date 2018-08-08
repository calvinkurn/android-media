package com.tokopedia.ride.ontrip.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.di.scope.BookingRideScope;
import com.tokopedia.ride.bookingride.domain.GetPendingAmountUseCase;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetCancelReasonsUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideProductUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;
import com.tokopedia.ride.ontrip.domain.UpdateRideRequestUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 7/26/17.
 */
@Module
public class OnTripModule {

    public OnTripModule() {
    }

    @Provides
    @OnTripScope
    CreateRideRequestUseCase provideGetPromoUseCase(ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutionThread,
                                                    BookingRideRepository bookingRideRepository) {
        return new CreateRideRequestUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    GetRideRequestMapUseCase provideGetRideRequestMapUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             BookingRideRepository bookingRideRepository) {
        return new GetRideRequestMapUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    GetRideRequestDetailUseCase provideGetRideRequestDetailUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   BookingRideRepository bookingRideRepository) {
        return new GetRideRequestDetailUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    GetRideProductUseCase provideGetRideProductUseCase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       BookingRideRepository bookingRideRepository) {
        return new GetRideProductUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    GetCancelReasonsUseCase provideGetCancelReasonsUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           BookingRideRepository bookingRideRepository) {
        return new GetCancelReasonsUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    CancelRideRequestUseCase provideCancelRideRequestUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             BookingRideRepository bookingRideRepository) {
        return new CancelRideRequestUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    UpdateRideRequestUseCase provideUpdateRideRequestUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             BookingRideRepository bookingRideRepository) {
        return new UpdateRideRequestUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @OnTripScope
    GetPendingAmountUseCase provideGetPendingAmountUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           BookingRideRepository bookingRideRepository) {
        return new GetPendingAmountUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }
}
