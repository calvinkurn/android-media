package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import rx.Observable;

/**
 * Created by alvarisi on 3/21/17.
 */

public class GetFareEstimateUseCase extends UseCase<FareEstimate> {
    private final BookingRideRepository bookingRideRepository;
    public GetFareEstimateUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<FareEstimate> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getEstimatedFare(requestParams.getParameters());
    }
}
