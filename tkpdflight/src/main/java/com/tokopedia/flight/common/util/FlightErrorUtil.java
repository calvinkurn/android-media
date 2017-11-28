package com.tokopedia.flight.common.util;

import com.tokopedia.abstraction.utils.ErrorHandler;
import com.tokopedia.flight.common.data.model.FlightException;

/**
 * Created by User on 11/28/2017.
 */

public class FlightErrorUtil {
    public static String getMessageFromException(Throwable e) {
        if (e instanceof FlightException) {
            return ((FlightException) e).getErrorList().get(0).getTitle();
        } else {
            return ErrorHandler.getErrorMessage(e);
        }
    }
}
