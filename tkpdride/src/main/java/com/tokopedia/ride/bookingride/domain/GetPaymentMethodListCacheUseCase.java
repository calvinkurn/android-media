package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethod;
import com.tokopedia.ride.common.ride.domain.model.PaymentMethodList;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;

import java.util.List;

import rx.Observable;

/**
 * Created by Vishal on 11/17/17.
 */

public class GetPaymentMethodListCacheUseCase extends UseCase<PaymentMethodList> {
    private final BookingRideRepository bookingRideRepository;

    public GetPaymentMethodListCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<PaymentMethodList> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getPaymentMethodListFromCache();
    }
}
