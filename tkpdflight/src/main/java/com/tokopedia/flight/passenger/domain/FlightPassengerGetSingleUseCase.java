package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.passenger.domain.model.SavedPassengerViewModelMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 14/03/18.
 */

public class FlightPassengerGetSingleUseCase extends UseCase<FlightBookingPassengerViewModel> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";

    private final FlightRepository flightRepository;
    private final SavedPassengerViewModelMapper savedPassengerViewModelMapper;

    @Inject
    public FlightPassengerGetSingleUseCase(FlightRepository flightRepository,
                                           SavedPassengerViewModelMapper savedPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.savedPassengerViewModelMapper = savedPassengerViewModelMapper;
    }

    @Override
    public Observable<FlightBookingPassengerViewModel> createObservable(RequestParams requestParams) {
        return flightRepository.getSinglePassengerById(
                requestParams.getString(PARAM_PASSENGER_ID, ""))
                .map(new Func1<FlightPassengerDb, FlightBookingPassengerViewModel>() {
                    @Override
                    public FlightBookingPassengerViewModel call(FlightPassengerDb flightPassengerDb) {
                        return savedPassengerViewModelMapper.transform(flightPassengerDb);
                    }
                });
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }
}
