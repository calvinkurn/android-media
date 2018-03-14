package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.passenger.domain.model.SavedPassengerViewModelMapper;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightBookingGetSavedPassengerUseCase extends UseCase<List<FlightBookingPassengerViewModel>> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";

    private final FlightRepository flightRepository;
    private final SavedPassengerViewModelMapper savedPassengerViewModelMapper;

    @Inject
    public FlightBookingGetSavedPassengerUseCase(FlightRepository flightRepository, SavedPassengerViewModelMapper savedPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.savedPassengerViewModelMapper = savedPassengerViewModelMapper;
    }

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPassengerList(requestParams.getString(PARAM_PASSENGER_ID, ""))
                .map(new Func1<List<FlightPassengerDb>, List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public List<FlightBookingPassengerViewModel> call(List<FlightPassengerDb> flightPassengerDbs) {
                        return savedPassengerViewModelMapper.transform(flightPassengerDbs);
                    }
                });
    }

    public RequestParams createEmptyRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, "");
        return requestParams;
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }
}
