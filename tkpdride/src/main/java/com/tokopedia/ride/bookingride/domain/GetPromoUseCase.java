package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.domain.model.Promo;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class GetPromoUseCase extends UseCase<List<Promo>> {
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";
    private final BookingRideRepository bookingRideRepository;

    public GetPromoUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<Promo>> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getPromo(requestParams.getParameters());
    }
}
