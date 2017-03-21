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
    public static final String PARAM_START_LATITUDE = "start_latitude";
    public static final String PARAM_START_LONGITUDE = "start_longitude";
    public static final String PARAM_END_LATITUDE = "end_latitude";
    public static final String PARAM_END_LONGITUDE = "end_longitude";
    public static final String PARAM_PRODUCT_ID = "product_id";
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
