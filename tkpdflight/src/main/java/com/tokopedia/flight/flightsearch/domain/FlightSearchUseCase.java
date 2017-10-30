package com.tokopedia.flight.flightsearch.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.flightsearch.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.flightsearch.data.repository.FlightSearchRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightSearchUseCase extends UseCase<List<FlightSearchSingleRouteDB>> {
    private final FlightSearchRepository flightSearchRepository;

    @Inject
    public FlightSearchUseCase(FlightSearchRepository flightSearchRepository) {
        this.flightSearchRepository = flightSearchRepository;
    }

    //TODO request params for usecase
    @Override
    public Observable<List<FlightSearchSingleRouteDB>> createObservable(RequestParams requestParams) {
        return flightSearchRepository.getFlightSearch(false);
    }

}
