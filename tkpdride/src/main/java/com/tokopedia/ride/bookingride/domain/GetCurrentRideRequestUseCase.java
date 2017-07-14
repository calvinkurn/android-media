package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Observable;

/**
 * Created by alvarisi on 4/21/17.
 */

public class GetCurrentRideRequestUseCase extends UseCase<RideRequest> {
    private final BookingRideRepository bookingRideRepository;
    public GetCurrentRideRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<RideRequest> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getCurrentRequest(requestParams.getParameters());
    }
}
