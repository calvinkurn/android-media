package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightAirportPickerUseCase extends UseCase<List<FlightAirportDB>> {
    private final FlightRepository flightRepository;

    public FlightAirportPickerUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightAirportDB>> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportList(requestParams);
    }
}
