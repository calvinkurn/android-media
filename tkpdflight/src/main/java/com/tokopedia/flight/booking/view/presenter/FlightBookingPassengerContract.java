package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * Created by alvarisi on 11/16/17.
 */

public interface FlightBookingPassengerContract {

    interface View extends CustomerView {

        FlightBookingPassengerViewModel getCurrentPassengerViewModel();

        void renderSpinnerForAdult();

        void renderSpinnerForChildAndInfant();

        String getReturnTripId();

        String getDepartureId();
    }

    interface Presenter extends CustomerPresenter<View> {

        void onViewCreated();
    }
}
