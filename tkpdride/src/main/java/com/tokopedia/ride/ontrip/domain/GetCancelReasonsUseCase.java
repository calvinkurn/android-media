package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 6/14/17.
 */

public class GetCancelReasonsUseCase extends UseCase<List<String>> {
    private BookingRideRepository bookingRideRepository;

    public GetCancelReasonsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getCancelReasons(requestParams.getParameters());
    }
}
