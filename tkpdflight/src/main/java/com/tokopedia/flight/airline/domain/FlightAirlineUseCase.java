package com.tokopedia.flight.airline.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightAirlineUseCase extends UseCase<FlightAirlineDB> {
    private static final String AIRLINE_ID = "airline_id";
    private final FlightRepository flightRepository;

    public FlightAirlineUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public static RequestParams createRequestParams(String airlineID) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(AIRLINE_ID, airlineID);
        return requestParams;
    }

    @Override
    public Observable<FlightAirlineDB> createObservable(RequestParams requestParams) {
        return flightRepository.getAirlineById(requestParams==null?null: requestParams.getString(AIRLINE_ID, null));
    }

    public RequestParams createRequestParam(String airlineID) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(AIRLINE_ID, airlineID);
        return requestParams;
    }
}
