package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Observable;

/**
 * Created by alvarisi on 3/24/17.
 */

public class CreateRideRequestUseCase extends UseCase<RideRequest> {
    public static final String PARAM_FARE_ID = "fare_id";
    public static final String PARAM_START_LONGITUDE = "start_longitude";
    public static final String PARAM_START_LATITUDE = "start_latitude";
    public static final String PARAM_END_LATITUDE = "end_latitude";
    public static final String PARAM_END_LONGITUDE = "end_longitude";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";

    private BookingRideRepository bookingRideRepository;
    public CreateRideRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<RideRequest> createObservable(RequestParams requestParams) {
        return bookingRideRepository.createRideRequest(requestParams.getParameters());
    }
}
