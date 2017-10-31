package com.tokopedia.flight.airline.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightAirlineUseCase extends UseCase<List<FlightAirlineDB>> {
    public static final String AIRLINE_ID = "airline_id";
    private final FlightRepository flightRepository;

    @Inject
    public FlightAirlineUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightAirlineDB>> createObservable(RequestParams requestParams) {
        return flightRepository.getAirlineList(requestParams.getString(AIRLINE_ID, null));
    }

    public static RequestParams createRequestParams(String airlineID) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(AIRLINE_ID, airlineID);
        return requestParams;
    }
}
