package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.PayPending;

import rx.Observable;

/**
 * Created by Vishal on 10th Nov, 2017.
 */

public class GetPayPendingDataUseCase extends UseCase<PayPending> {
    private final BookingRideRepository mBookingRideRepository;


    public GetPayPendingDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        mBookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<PayPending> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.payPendingAmount();
    }
}
