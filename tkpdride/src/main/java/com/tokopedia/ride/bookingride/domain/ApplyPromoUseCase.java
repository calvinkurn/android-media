package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;

import rx.Observable;

/**
 * Created by alvarisi on 4/25/17.
 */

public class ApplyPromoUseCase extends UseCase<ApplyPromo> {
    private final BookingRideRepository bookingRideRepository;
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";
    public static final String PARAM_PROMO = "promo";

    public ApplyPromoUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<ApplyPromo> createObservable(RequestParams requestParams) {
        return bookingRideRepository.applyPromo(requestParams.getParameters());
    }
}
