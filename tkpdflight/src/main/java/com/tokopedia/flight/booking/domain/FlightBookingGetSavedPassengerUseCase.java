package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.booking.domain.model.SavedPassengerViewModelMapper;
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

    private final FlightRepository flightRepository;
    private final SavedPassengerViewModelMapper savedPassengerViewModelMapper;

    @Inject
    public FlightBookingGetSavedPassengerUseCase(FlightRepository flightRepository, SavedPassengerViewModelMapper savedPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.savedPassengerViewModelMapper = savedPassengerViewModelMapper;
    }

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getSavedPassenger()
                .map(new Func1<List<SavedPassengerEntity>, List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public List<FlightBookingPassengerViewModel> call(List<SavedPassengerEntity> savedPassengerEntities) {
                        return savedPassengerViewModelMapper.transform(savedPassengerEntities);
                    }
                });
    }

    public RequestParams createEmptyRequestParams() {
        return RequestParams.EMPTY;
    }
}
