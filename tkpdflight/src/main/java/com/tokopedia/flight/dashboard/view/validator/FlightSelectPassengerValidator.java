package com.tokopedia.flight.dashboard.view.validator;

import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/26/17.
 */

public class FlightSelectPassengerValidator {
    private static final int MAX_PASSENGER_VALUE = 7;

    @Inject
    public FlightSelectPassengerValidator() {
    }

    public boolean validateTotalPassenger(FlightPassengerViewModel passData) {
        int total = passData.getAdult() + passData.getChildren();
        return total <= MAX_PASSENGER_VALUE;
    }

    public boolean validateInfantNotGreaterThanAdult(FlightPassengerViewModel passengerPassData) {
        return passengerPassData.getInfant() <= passengerPassData.getAdult();
    }

    public boolean validateAdultCountAtleastOne(FlightPassengerViewModel passengerPassData) {
        return passengerPassData.getAdult() > 0;
    }
}
