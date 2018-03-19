package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.Date;

/**
 * @author by furqan on 12/03/18.
 */

public interface FlightPassengerUpdateContract {

    interface View extends CustomerView {

        String getString(int resId);

        String getDepartureDate();

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        String getPassengerBirthdate();

        void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel);

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        void renderSelectedTitle(String passengerTitle);

        void renderPassengerType(String typeText);

        void renderPassengerName(String firstName, String lastName);

        void renderPassengerBirthdate(String birthdate);

        void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

        void showBirthdatePickerDialog(Date selectedDate, Date maxDate);

    }

    interface Presenter {

        void onViewCreated();

        void onBirthdateClicked();

        void onBirthdateChanged(int year, int month, int date, Date minDate, Date maxDate);

        void onBirthdateChanged(int year, int month, int date, Date maxDate);
    }

}
