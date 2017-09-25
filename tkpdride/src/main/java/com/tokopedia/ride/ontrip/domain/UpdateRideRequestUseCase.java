package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.UpdateDestination;

import rx.Observable;

/**
 * Created by alvarisi on 5/12/17.
 */

public class UpdateRideRequestUseCase extends UseCase<UpdateDestination> {
    public static final String PARAM_END_LATITUDE = "end_latitude";
    public static final String PARAM_END_LONGITUDE = "end_longitude";
    public static final String PARAM_END_ADD_NAME = "end_address_name";
    public static final String PARAM_END_ADD = "end_address";
    public static final String PARAM_REQUEST_ID = "request_id";

    private BookingRideRepository bookingRideRepository;

    public UpdateRideRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<UpdateDestination> createObservable(RequestParams requestParams) {
        return bookingRideRepository.updateRequest(requestParams.getParameters());
    }
}
