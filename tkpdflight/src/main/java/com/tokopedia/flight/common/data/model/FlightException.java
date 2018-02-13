package com.tokopedia.flight.common.data.model;

import java.io.IOException;
import java.util.List;

/**
 * Created by User on 11/28/2017.
 */

public class FlightException extends IOException {
    private List<FlightError> errorList;

    public List<FlightError> getErrorList() {
        return errorList;
    }

    public FlightException(List<FlightError> errorList) {
        this.errorList = errorList;
    }

}
