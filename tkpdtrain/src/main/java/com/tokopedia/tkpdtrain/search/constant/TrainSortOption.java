package com.tokopedia.tkpdtrain.search.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Rizky on 15/03/18.
 */

@IntDef({
        TrainSortOption.NO_PREFERENCE,
        TrainSortOption.EARLIEST_DEPARTURE,
        TrainSortOption.LATEST_DEPARTURE,
        TrainSortOption.SHORTEST_DURATION,
        TrainSortOption.LONGEST_DURATION,
        TrainSortOption.EARLIEST_ARRIVAL,
        TrainSortOption.LATEST_ARRIVAL,
        TrainSortOption.CHEAPEST,
        TrainSortOption.MOST_EXPENSIVE})
@Retention(RetentionPolicy.SOURCE)
public @interface TrainSortOption {
    int NO_PREFERENCE = 0;
    int EARLIEST_DEPARTURE = 1;
    int LATEST_DEPARTURE = 2;
    int SHORTEST_DURATION = 3;
    int LONGEST_DURATION = 4;
    int EARLIEST_ARRIVAL = 5;
    int LATEST_ARRIVAL = 6;
    int CHEAPEST = 7;
    int MOST_EXPENSIVE = 8;
}
