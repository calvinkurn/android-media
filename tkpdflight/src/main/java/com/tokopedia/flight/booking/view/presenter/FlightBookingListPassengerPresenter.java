package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSavedPassengerUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingListPassengerPresenter extends BaseDaggerPresenter<FlightBookingListPassengerContract.View> implements FlightBookingListPassengerContract.Presenter {

    private FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase;

    @Inject
    public FlightBookingListPassengerPresenter(FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase) {
        this.flightBookingGetSavedPassengerUseCase = flightBookingGetSavedPassengerUseCase;
    }


    @Override
    public void onViewCreated() {
        getSavedPassengerList();
    }

    private void getSavedPassengerList() {
        flightBookingGetSavedPassengerUseCase.execute(
                flightBookingGetSavedPassengerUseCase.createEmptyRequestParams(),
                new Subscriber<List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<FlightBookingPassengerViewModel> flightBookingPassengerViewModels) {
                        getView().setPassengerViewModelList(flightBookingPassengerViewModels);
                    }
                }
        );
    }
}
