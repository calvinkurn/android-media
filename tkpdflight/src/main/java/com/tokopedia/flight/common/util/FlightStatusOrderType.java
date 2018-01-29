package com.tokopedia.flight.common.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zulfikarrahman on 12/14/17.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({FlightStatusOrderType.EXPIRED,
        FlightStatusOrderType.WAITING_FOR_PAYMENT,
        FlightStatusOrderType.WAITING_FOR_THIRD_PARTY,
        FlightStatusOrderType.WAITING_FOR_TRANSFER,
        FlightStatusOrderType.READY_FOR_QUEUE,
        FlightStatusOrderType.PROGRESS,
        FlightStatusOrderType.FAILED,
        FlightStatusOrderType.REFUNDED,
        FlightStatusOrderType.CONFIRMED,
        FlightStatusOrderType.FINISHED})
public @interface FlightStatusOrderType {
    int EXPIRED = 0;
    int WAITING_FOR_PAYMENT = 100;
    int WAITING_FOR_THIRD_PARTY = 101;
    int WAITING_FOR_TRANSFER = 102;
    int READY_FOR_QUEUE = 200;
    int PROGRESS = 300;
    int FAILED = 600;
    int REFUNDED = 650;
    int CONFIRMED = 700;
    int FINISHED = 800;
}

