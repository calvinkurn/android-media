package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;

import rx.Observable;

/**
 * Created by Vishal on 11/17/17.
 */

public class GetPaymentMethodListUseCase extends UseCase<PaymentMethodList> {
    private final BookingRideRepository mBookingRideRepository;
    public static String PARAM_PAYMENT_METHOD = "payment_method";


    public GetPaymentMethodListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        mBookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<PaymentMethodList> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.getPaymentMethodList(requestParams.getParameters());
    }
}
