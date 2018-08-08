package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.TimesEstimate;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/20/17.
 */

public class GetEstimatedTimesUseCase extends UseCase<List<TimesEstimate>> {
    private final BookingRideRepository mBookingRideRepository;
    public static final String PARAM_START_LATITUDE = "start_latitude";
    public static final String PARAM_START_LONGITUDE = "start_longitude";

    public GetEstimatedTimesUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository mBookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.mBookingRideRepository = mBookingRideRepository;
    }

    @Override
    public Observable<List<TimesEstimate>> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.getEstimatedTimes(requestParams.getParameters());
    }
}
