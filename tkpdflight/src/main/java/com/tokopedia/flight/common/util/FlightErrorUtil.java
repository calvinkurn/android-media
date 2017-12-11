package com.tokopedia.flight.common.util;

import android.text.TextUtils;

import com.tokopedia.abstraction.utils.ErrorHandler;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;

/**
 * Created by User on 11/28/2017.
 */

public class FlightErrorUtil {
    public static String getMessageFromException(Throwable e) {
        if (e instanceof FlightException) {
//            return ((FlightException) e).getErrorList().get(0).getTitle();
            return TextUtils.join(",", ((FlightException) e).getErrorList());
        } else {
            return ErrorHandler.getErrorMessage(e);
        }
    }

    public static int getErrorCode(FlightError flightError){
        try {
            return Integer.parseInt(flightError.getId());
        }catch (Exception e){
            return -1;
        }
    }
}
