package com.tokopedia.flight.common.util;

/**
 * Created by zulfikarrahman on 12/14/17.
 */

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FlightAmenityType.LUGGAGE, FlightAmenityType.MEAL})
public @interface FlightAmenityType {
    String LUGGAGE =  "1";
    String MEAL = "2";
}
