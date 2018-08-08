package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Observable;

/**
 * Created by alvarisi on 3/24/17.
 */

public class CreateRideRequestUseCase extends UseCase<RideRequest> {
    public static final String PARAM_FARE_ID = "fare_id";
    public static final String PARAM_START_LONGITUDE = "start_longitude";
    public static final String PARAM_START_LATITUDE = "start_latitude";
    public static final String PARAM_END_LATITUDE = "end_latitude";
    public static final String PARAM_END_LONGITUDE = "end_longitude";
    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_DEVICE_ID = "device_id";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String PARAM_TIMESTAMP = "device_time";
    public static final String PARAM_START_ADDRESS_NAME = "start_address_name";
    public static final String PARAM_START_ADDRESS = "start_address";
    public static final String PARAM_END_ADDRESS_NAME = "end_address_name";
    public static final String PARAM_END_ADDRESS = "end_address";
    public static final String PARAM_SURGE_CONFIRMATION_ID = "surge_confirmation_id";
    public static final String PARAM_PRODUCT_ID = "product_id";

    public static final String PARAM_PROMO_CODE = "promocode";
    public static final String PARAM_PRODUCT_NAME = "product_name";
    public static final String PARAM_START_PLACE_ID = "start_place_id";
    public static final String PARAM_END_PLACE_ID = "end_place_id";
    public static final String PARAM_DEVICE_TYPE = "device_type";
    public static final String PARAM_APP_LATITUDE = "alat";
    public static final String PARAM_APP_LONGITUDE = "along";
    public static final String PARAM_API_VERSION = "api_version";

    private BookingRideRepository bookingRideRepository;

    public CreateRideRequestUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                    BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<RideRequest> createObservable(RequestParams requestParams) {
        return bookingRideRepository.createRequest(requestParams.getParameters());
    }
}
