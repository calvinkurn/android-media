package com.tokopedia.ride.ontrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.CancelReasons;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 6/14/17.
 */

public class GetCancelReasonsUseCase extends UseCase<List<String>> {
    private BookingRideRepository bookingRideRepository;

    public GetCancelReasonsUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository bookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.bookingRideRepository = bookingRideRepository;
    }

    @Override
    public Observable<List<String>> createObservable(RequestParams requestParams) {
        return bookingRideRepository.getCancelReasons(requestParams.getParameters())
                .map(new Func1<CancelReasons, List<String>>() {
                    @Override
                    public List<String> call(CancelReasons cancelReasons) {
                        return cancelReasons.getReasons();
                    }
                });
    }
}
