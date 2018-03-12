package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 12/03/18.
 */

public interface FlightPassengerUpdateContract {

    interface View extends CustomerView {

        String getPassengerId();

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel);

    }

    interface Presenter {



    }

}
