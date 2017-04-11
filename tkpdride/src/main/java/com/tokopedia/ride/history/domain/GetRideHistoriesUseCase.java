package com.tokopedia.ride.history.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 4/11/17.
 */

public class GetRideHistoriesUseCase extends UseCase<List<RideHistory>> {
    private BookingRideRepository bookingRideRepository;

    public GetRideHistoriesUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<RideHistory>> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getHistories(requestParams.getParameters());
    }
}
