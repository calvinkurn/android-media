package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 05/03/18.
 */

public class FlightBookingDeleteAllPassengerListUseCase extends UseCase<Boolean> {

    FlightRepository flightRepository;

    @Inject
    public FlightBookingDeleteAllPassengerListUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.deleteAllListPassenger();
    }

    public RequestParams createEmptyRequestParams() {
        return RequestParams.EMPTY;
    }
}
