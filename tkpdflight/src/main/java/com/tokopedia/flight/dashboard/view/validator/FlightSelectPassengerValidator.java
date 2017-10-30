package com.tokopedia.flight.dashboard.view.validator;

import com.tokopedia.flight.dashboard.view.fragment.viewmodel.SelectFlightPassengerViewModel;

import javax.inject.Inject;

/**
 * Created by alvarisi on 10/26/17.
 */

public class FlightSelectPassengerValidator {
    private static final int MAX_PASSENGER_VALUE = 7;

    @Inject
    public FlightSelectPassengerValidator() {
    }

    public boolean validateTotalPassenger(SelectFlightPassengerViewModel passData) {
        int total = passData.getAdult() + passData.getChildren() + passData.getInfant();
        return total <= MAX_PASSENGER_VALUE;
    }

    public boolean validateInfantNotGreaterThanAdult(SelectFlightPassengerViewModel passengerPassData) {
        return passengerPassData.getInfant() <= passengerPassData.getAdult();
    }

    public boolean validateAdultCountAtleastOne(SelectFlightPassengerViewModel passengerPassData) {
        return passengerPassData.getAdult() > 0;
    }
}
