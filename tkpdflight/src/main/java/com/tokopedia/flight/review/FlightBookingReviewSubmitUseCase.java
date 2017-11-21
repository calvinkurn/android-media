package com.tokopedia.flight.review;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewSubmitUseCase extends UseCase<Boolean> {

    @Inject
    public FlightBookingReviewSubmitUseCase() {
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return null;
    }
}
