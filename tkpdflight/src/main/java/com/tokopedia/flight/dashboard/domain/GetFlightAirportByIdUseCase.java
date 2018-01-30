package com.tokopedia.flight.dashboard.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by furqan on 22/01/18.
 */

public class GetFlightAirportByIdUseCase extends UseCase<FlightAirportDB> {
    private static final String PARAM_AIRPORT_ID = "PARAM_AIRPORT_ID";
    private static final String DEFAULT_AIRPORT_ID = "JKTA";

    private FlightRepository flightRepository;

    @Inject
    public GetFlightAirportByIdUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightAirportDB> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportById(requestParams.getString(PARAM_AIRPORT_ID, DEFAULT_AIRPORT_ID));
    }

    public RequestParams createRequestParams(String airportId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_AIRPORT_ID, airportId);
        return requestParams;
    }
}
