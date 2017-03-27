package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import rx.Observable;

/**
 * Created by alvarisi on 3/27/17.
 */

public class CancelRideRequestUseCase extends UseCase<String> {
    public static final String PARAM_REQUEST_ID = "request_id";
    private BookingRideRepository bookingRideRepository;

    public CancelRideRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return bookingRideRepository.cancelRequest(requestParams.getParameters());
    }
}
