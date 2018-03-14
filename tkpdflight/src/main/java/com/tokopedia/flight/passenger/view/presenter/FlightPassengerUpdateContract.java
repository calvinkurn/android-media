package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.domain.FlightPassengerGetSingleUseCase;

/**
 * @author by furqan on 12/03/18.
 */

public interface FlightPassengerUpdateContract {

    interface View extends CustomerView {

        String getPassengerId();

        String getString(int resId);

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel);

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        void renderSelectedTitle(String passengerTitle);

        void renderPassengerType(String typeText);

        void renderPassengerName(String firstName, String lastName);

        void renderPassengerBirthdate(String birthdate);

    }

    interface Presenter {

        void onViewCreated();

    }

}
