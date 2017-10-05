package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.Product;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public class GetUberProductsUseCase extends UseCase<List<Product>> {
    private final BookingRideRepository mBookingRideRepository;
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_LONGITUDE = "longitude";

    public GetUberProductsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        mBookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<Product>> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.getProducts(requestParams.getParameters());
    }
}
