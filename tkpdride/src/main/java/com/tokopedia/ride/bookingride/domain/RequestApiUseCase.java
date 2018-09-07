package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import rx.Observable;

/**
 * Created by Vishal on 11/6/17
 */

public class RequestApiUseCase extends UseCase<String> {
    private final BookingRideRepository mBookingRideRepository;
    private String mUrl;


    public RequestApiUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        mBookingRideRepository = bookingRideRepository;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.requestApi(mUrl, requestParams.getParameters());
    }
}
