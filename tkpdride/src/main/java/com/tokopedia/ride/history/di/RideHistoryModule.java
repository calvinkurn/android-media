package com.tokopedia.ride.history.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.di.scope.BookingRideScope;
import com.tokopedia.ride.bookingride.domain.GetPayPendingDataUseCase;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.history.domain.GetHistoriesWithPaginationUseCase;
import com.tokopedia.ride.history.domain.GetRideHistoriesUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 7/26/17.
 */
@Module
public class RideHistoryModule {
    public RideHistoryModule() {
    }

    @Provides
    @RideHistoryScope
    GetHistoriesWithPaginationUseCase provideGetUberProductsUseCase(ThreadExecutor threadExecutor,
                                                                    PostExecutionThread postExecutionThread,
                                                                    BookingRideRepository bookingRideRepository) {
        return new GetHistoriesWithPaginationUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @RideHistoryScope
    GetRideHistoriesUseCase provideGetRideHistoriesUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           BookingRideRepository bookingRideRepository) {
        return new GetRideHistoriesUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @RideHistoryScope
    GetSingleRideHistoryUseCase provideGetSingleRideHistoryUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               BookingRideRepository bookingRideRepository) {
        return new GetSingleRideHistoryUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @RideHistoryScope
    GetPayPendingDataUseCase provideGetPayPendingDataUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             BookingRideRepository bookingRideRepository) {
        return new GetPayPendingDataUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }
}
