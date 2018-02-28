package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.List;

/**
 * @author by furqan on 26/02/18.
 */

public interface FlightBookingListPassengerContract {
    interface View extends CustomerView {
        List<FlightBookingPassengerViewModel> getPassengerViewModelList();

        void setPassengerViewModelList(List<FlightBookingPassengerViewModel> passengerViewModelList);

        void renderPassengerList();

        FlightBookingPassengerViewModel getCurrentPassenger();

        String getSalutationString(int resId);
    }

    interface Presenter {
        void onViewCreated();

        boolean isPassengerSame(FlightBookingPassengerViewModel selectedPassenger);
    }
}
