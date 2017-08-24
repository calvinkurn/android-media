package com.tokopedia.ride.completetrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.completetrip.domain.model.TipList;

import rx.Observable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class GetTipListUseCase extends UseCase<TipList> {
    private final BookingRideRepository mBookingRideRepository;

    public GetTipListUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository mBookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.mBookingRideRepository = mBookingRideRepository;
    }

    @Override
    public Observable<TipList> createObservable(RequestParams params) {
        return mBookingRideRepository.getTipList();
    }
}
