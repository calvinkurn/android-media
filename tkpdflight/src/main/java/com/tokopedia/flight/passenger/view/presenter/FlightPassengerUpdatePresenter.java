package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSavedPassengerUseCase;

import javax.inject.Inject;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdatePresenter extends BaseDaggerPresenter<FlightPassengerUpdateContract.View>
        implements FlightPassengerUpdateContract.Presenter{

    FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase;

    @Inject
    public FlightPassengerUpdatePresenter(FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase) {
        this.flightBookingGetSavedPassengerUseCase = flightBookingGetSavedPassengerUseCase;
    }

    @Override
    public void onViewCreated() {

    }

}
