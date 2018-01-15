package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightAirportPickerUseCase extends UseCase<List<FlightAirportDB>> {
    public static final String KEYWORD = "keyword";
    public static final String ID_COUNTRY_INDONESIA = "ID";
    private final FlightRepository flightRepository;

    @Inject
    public FlightAirportPickerUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public static RequestParams createRequestParams(String text) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEYWORD, text);
        return requestParams;
    }

    @Override
    public Observable<List<FlightAirportDB>> createObservable(RequestParams requestParams) {
        return flightRepository.getAirportList(requestParams.getString(KEYWORD, ""), ID_COUNTRY_INDONESIA);
    }

    public RequestParams createRequestParam(String text) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEYWORD, text);
        return requestParams;
    }
}
