package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 02/03/18.
 */

public class FlightBookingUpdateSelectedPassengerUseCase extends UseCase<Boolean> {

    private final static String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private final static String PARAM_IS_SELECTED = "PARAM_IS_SELECTED";
    private final static String DEFAULT_PASSENGER_ID = "";
    private final static int DEFAULT_IS_SELECTED = 0;

    FlightRepository flightRepository;

    @Inject
    public FlightBookingUpdateSelectedPassengerUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return flightRepository.updateIsSelected(
                requestParams.getString(PARAM_PASSENGER_ID, DEFAULT_PASSENGER_ID),
                requestParams.getInt(PARAM_IS_SELECTED, DEFAULT_IS_SELECTED)
        );
    }

    public RequestParams createRequestParams(String passengerId, int isSelected) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        requestParams.putInt(PARAM_IS_SELECTED, isSelected);
        return requestParams;
    }

}
