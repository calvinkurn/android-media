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
    private static final String PARAM_AIRPORT_ID = "PARAM_AIRPORT_ID";
    public static final String AIRPORT_ID = "AIRPORT_ID";
    public static final String CITY_CODE = "CITY_CODE";
    private static final String DEFAULT_AIRPORT_ID = "CGK";

    private FlightRepository flightRepository;

    @Inject
    public GetFlightAirportWithParamUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightAirportDB> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportWithParam((Map<String, String>) requestParams.getObject(PARAM_AIRPORT_ID));
    }

    public RequestParams createRequestParams(String paramId) {
        Map<String, String> mapParam = new HashMap<>();
        if (paramId.length() > 3) {
            mapParam.put(CITY_CODE, paramId);
        } else {
            mapParam.put(AIRPORT_ID, paramId);
        }

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_AIRPORT_ID, mapParam);
        return requestParams;
    }
}
