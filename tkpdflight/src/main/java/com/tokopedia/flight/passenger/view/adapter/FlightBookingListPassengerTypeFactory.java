package com.tokopedia.flight.passenger.view.adapter;

import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerTypeFactory;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;

/**
 * @author by furqan on 07/03/18.
 */

public interface FlightBookingListPassengerTypeFactory extends FlightBookingPassengerTypeFactory {

    int type(FlightBookingNewPassengerViewModel viewModel);

}
