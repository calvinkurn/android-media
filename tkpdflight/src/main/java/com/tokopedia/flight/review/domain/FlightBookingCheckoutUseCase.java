package com.tokopedia.flight.review.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by zulfikarrahman on 11/10/17.
 */

public class FlightBookingCheckoutUseCase extends UseCase<Boolean> {

    @Inject
    public FlightBookingCheckoutUseCase() {
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return null;
    }
}
