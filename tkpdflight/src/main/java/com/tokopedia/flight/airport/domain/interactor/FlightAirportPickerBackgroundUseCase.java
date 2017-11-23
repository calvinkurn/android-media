package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class FlightAirportPickerBackgroundUseCase extends UseCase<Boolean> {

    private final FlightRepository flightRepository;

    @Inject
    public FlightAirportPickerBackgroundUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportListBackground();
    }
}
