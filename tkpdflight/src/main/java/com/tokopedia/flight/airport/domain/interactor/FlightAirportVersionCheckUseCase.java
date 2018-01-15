package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 12/8/17.
 */

public class FlightAirportVersionCheckUseCase extends UseCase<Boolean> {

    public static final String CURRENT_VERSION = "CURRENT_VERSION";
    private FlightRepository flightRepository;

    @Inject
    public FlightAirportVersionCheckUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.checkVersionAirport(requestParams.getLong(CURRENT_VERSION, 0));
    }

    public RequestParams createRequestParams(long currentVersion){
        RequestParams requestParams = RequestParams.create();
        requestParams.putLong(CURRENT_VERSION, currentVersion);
        return requestParams;
    }
}
