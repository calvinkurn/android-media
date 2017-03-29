package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Observable;

/**
 * Created by alvarisi on 3/29/17.
 */

public class GetCurrentDetailRideRequestUseCase extends UseCase<RideRequest> {
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";
    public static final String PARAM_REQUEST_ID = "request_id";

    private BookingRideRepository bookingRideRepository;
    public GetCurrentDetailRideRequestUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<RideRequest> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getCurrentRequest(requestParams.getParameters());
    }
}
