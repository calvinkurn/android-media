package com.tokopedia.flight.search.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by alvarisi on 11/1/17.
 */
@IntDef({
        FlightSortOption.NO_PREFERENCE,
        FlightSortOption.EARLIEST_DEPARTURE,
        FlightSortOption.LATEST_DEPARTURE,
        FlightSortOption.EARLIEST_ARRIVAL,
        FlightSortOption.LATEST_ARRIVAL,
        FlightSortOption.SHORTEST_DURATION,
        FlightSortOption.LONGEST_DURATION,
        FlightSortOption.CHEAPEST,
        FlightSortOption.MOST_EXPENSIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightSortOption {
    int NO_PREFERENCE = 0;
    int EARLIEST_DEPARTURE = 1;
    int LATEST_DEPARTURE = 2;
    int EARLIEST_ARRIVAL = 3;
    int LATEST_ARRIVAL = 4;
    int SHORTEST_DURATION = 5;
    int LONGEST_DURATION = 6;
    int CHEAPEST = 7;
    int MOST_EXPENSIVE = 8;
}
