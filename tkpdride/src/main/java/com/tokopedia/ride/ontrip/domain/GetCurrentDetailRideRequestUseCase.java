package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import rx.Observable;

/**
 * Created by alvarisi on 3/29/17.
 */

public class GetCurrentDetailRideRequestUseCase extends UseCase<String> {
    private BookingRideRepository bookingRideRepository;
    public GetCurrentDetailRideRequestUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getCurrentRequest(requestParams.getParameters());
    }
}
