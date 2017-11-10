package com.tokopedia.ride.completetrip.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.domain.GetPayPendingDataUseCase;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.completetrip.domain.GetReceiptUseCase;
import com.tokopedia.ride.completetrip.domain.SendTipUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 7/26/17.
 */
@Module
public class CompleteTripModule {
    @Provides
    @CompleteTripScope
    GetReceiptUseCase provideGetReceiptUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               BookingRideRepository bookingRideRepository) {
        return new GetReceiptUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @CompleteTripScope
    GetRideRequestDetailUseCase provideGetRideRequestDetailUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   BookingRideRepository bookingRideRepository) {
        return new GetRideRequestDetailUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @CompleteTripScope
    GetSingleRideHistoryUseCase provideGetSingleRideHistoryUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   BookingRideRepository bookingRideRepository) {
        return new GetSingleRideHistoryUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @CompleteTripScope
    SendTipUseCase provideSendTipUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         BookingRideRepository bookingRideRepository) {
        return new SendTipUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }

    @Provides
    @CompleteTripScope
    GetPayPendingDataUseCase provideGetPayPendingDataUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             BookingRideRepository bookingRideRepository) {
        return new GetPayPendingDataUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
    }
}
