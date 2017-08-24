package com.tokopedia.ride.completetrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;

import rx.Observable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class SendTipUseCase extends UseCase<String> {
    private final BookingRideRepository mBookingRideRepository;

    public SendTipUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository mBookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.mBookingRideRepository = mBookingRideRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams params) {
        return mBookingRideRepository.sendTip(params.getParameters());
    }
}
