package com.tokopedia.flight.booking.constant;

/**
 * Created by alvarisi on 11/7/17.
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FlightBookingPassenger.ADULT, FlightBookingPassenger.CHILDREN, FlightBookingPassenger.INFANT})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightBookingPassenger {
    int ADULT = 1;
    int CHILDREN = 2;
    int INFANT = 3;
}
