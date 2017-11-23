package com.tokopedia.ride.completetrip.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.ride.domain.BookingRideRepository;
import com.tokopedia.ride.common.ride.domain.model.Receipt;

import rx.Observable;

/**
 * Created by alvarisi on 3/31/17.
 */

public class GetReceiptUseCase extends UseCase<Receipt> {
    public static final String PARAM_REQUEST_ID = "request_id";
    private final BookingRideRepository mBookingRideRepository;
    public GetReceiptUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BookingRideRepository mBookingRideRepository) {
        super(threadExecutor, postExecutionThread);
        this.mBookingRideRepository = mBookingRideRepository;
    }

    @Override
    public Observable<Receipt> createObservable(RequestParams requestParams) {
        return mBookingRideRepository.getReceipt(requestParams.getParameters());
    }
}
