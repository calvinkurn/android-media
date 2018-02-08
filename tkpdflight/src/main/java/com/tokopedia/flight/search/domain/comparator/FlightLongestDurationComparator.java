package com.tokopedia.flight.search.domain.comparator;

import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Comparator;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightLongestDurationComparator implements Comparator<FlightSearchViewModel> {
    @Override
    public int compare(FlightSearchViewModel first, FlightSearchViewModel second) {
        return second.getDurationMinute() - first.getDurationMinute();
    }
}
