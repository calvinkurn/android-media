package com.tokopedia.tkpdtrain.homepage.presentation;

import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;

/**
 * @author Rizky on 13/03/18.
 */

public class TrainPassengerPickerValidator {

    public boolean validateTotalPassenger(TrainPassengerViewModel passengerPassData) {
        return (passengerPassData.getAdult() + passengerPassData.getInfant()) <= 8;
    }

    public boolean validateAdultNotGreaterThanFour(TrainPassengerViewModel passengerPassData) {
        return passengerPassData.getAdult() <= 4;
    }

    public boolean validateInfantNotGreaterThanAdult(TrainPassengerViewModel passengerPassData) {
        return passengerPassData.getInfant() <= passengerPassData.getAdult();
    }

    public boolean validateAdultCountAtleastOne(TrainPassengerViewModel passengerPassData) {
        return passengerPassData.getAdult() >= 1;
    }

}
