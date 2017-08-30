package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideAddress;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 5/31/17.
 */

public class GetUserAddressCacheUseCase extends UseCase<List<RideAddress>> {
    private final BookingRideRepository bookingRideRepository;

    public GetUserAddressCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<RideAddress>> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getAddressesFromCache();
    }
}
