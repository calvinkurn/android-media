package com.tokopedia.flight.dashboard.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 22/01/18.
 */

public class GetFlightAirportWithParamUseCase extends UseCase<FlightAirportDB> {
    private static final String AIRPORT_ID = "AIRPORT_ID";
    private static final String CITY_CODE = "CITY_CODE";
    private static final int AIRPORT_CODE_DEFAULT_LENGTH = 3;

    private FlightRepository flightRepository;

    @Inject
    public GetFlightAirportWithParamUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightAirportDB> createObservable(RequestParams requestParams) {

        return flightRepository.getAirportWithParam(requestParams.getParamsAllValueInString());
    }

    public RequestParams createRequestParams(String paramId) {
        RequestParams requestParams = RequestParams.create();
        if (paramId.length() > AIRPORT_CODE_DEFAULT_LENGTH) {
            requestParams.putString(CITY_CODE, paramId);
        } else {
            requestParams.putString(AIRPORT_ID, paramId);
        }
        return requestParams;
    }
}
