package com.tokopedia.ride.history.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideHistoryWrapper;

import rx.Observable;

/**
 * Created by alvarisi on 7/19/17.
 */

public class GetHistoriesWithPaginationUseCase extends UseCase<RideHistoryWrapper> {
    private final BookingRideRepository bookingRideRepository;

    public GetHistoriesWithPaginationUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<RideHistoryWrapper> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getHistoriesWithPagination(requestParams.getParameters());
    }
}
