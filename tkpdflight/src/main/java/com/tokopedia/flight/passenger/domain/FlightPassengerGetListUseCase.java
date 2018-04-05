package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.passenger.domain.model.ListPassengerViewModelMapper;
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

public class FlightPassengerGetListUseCase extends UseCase<List<FlightBookingPassengerViewModel>> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private static final String DEFAULT_STRING_VALUE = "";

    private final FlightRepository flightRepository;
    private final ListPassengerViewModelMapper listPassengerViewModelMapper;

    @Inject
    public FlightPassengerGetListUseCase(FlightRepository flightRepository, ListPassengerViewModelMapper listPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.listPassengerViewModelMapper = listPassengerViewModelMapper;
    }

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPassengerList(requestParams.getString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE))
                .map(new Func1<List<FlightPassengerDb>, List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public List<FlightBookingPassengerViewModel> call(List<FlightPassengerDb> flightPassengerDbs) {
                        return listPassengerViewModelMapper.transform(flightPassengerDbs);
                    }
                });
    }

    public RequestParams createEmptyRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE);
        return requestParams;
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }
}
