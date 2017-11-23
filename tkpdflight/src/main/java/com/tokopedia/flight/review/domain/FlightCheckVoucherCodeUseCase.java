package com.tokopedia.flight.review.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightCheckVoucherCodeUseCase extends UseCase<Boolean> {

    private final FlightRepository flightRepository;

    @Inject
    public FlightCheckVoucherCodeUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return null;
    }
}
