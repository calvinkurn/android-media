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

    @Override
    public boolean isPassengerSame(FlightBookingPassengerViewModel selectedPassenger) {
        FlightBookingPassengerViewModel currentPassenger = getView().getCurrentPassenger();

        if (currentPassenger.getPassengerId() != null &&
                selectedPassenger.getPassengerId() != null) {
            if (currentPassenger.getPassengerId()
                    .equals(selectedPassenger.getPassengerId())) {
                return true;
            }
        }

        if (currentPassenger.getPassengerBirthdate() != null &&
                selectedPassenger.getPassengerBirthdate() != null) {
            if (currentPassenger.getPassengerBirthdate()
                    .equals(selectedPassenger.getPassengerBirthdate()) &&
                currentPassenger.getPassengerFirstName()
                    .equals(selectedPassenger.getPassengerFirstName()) &&
                currentPassenger.getPassengerLastName()
                    .equals(selectedPassenger.getPassengerLastName())) {
                return true;
            }
        }

        if (currentPassenger.getPassengerFirstName()
                .equals(selectedPassenger.getPassengerFirstName()) &&
            currentPassenger.getPassengerLastName()
                .equals(selectedPassenger.getPassengerLastName())) {
            return true;
        }

        return false;
    }

    private void getSavedPassengerList() {
        flightBookingGetSavedPassengerUseCase.execute(
                flightBookingGetSavedPassengerUseCase.createEmptyRequestParams(),
                new Subscriber<List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<FlightBookingPassengerViewModel> flightBookingPassengerViewModels) {
                        getView().setPassengerViewModelList(flightBookingPassengerViewModels);
                        getView().renderPassengerList();
                    }
                }
        );
    }
}
