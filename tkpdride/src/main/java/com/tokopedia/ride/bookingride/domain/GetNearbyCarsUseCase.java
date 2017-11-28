package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import rx.Observable;

/**
 * Created by Vishal on 3/14/17.
 */

public class GetNearbyCarsUseCase extends UseCase<NearbyRides> {
    private final BookingRideRepository mBookingRideRepository;
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_LONGITUDE = "longitude";
    public static String PARAM_CAR_ETA = "car_eta";
    public static String PARAM_BIKE_ETA = "bike_eta";


    public GetNearbyCarsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        mBookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<NearbyRides> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.getNearbyCars(requestParams.getParameters());
    }
}
