package com.tokopedia.flight.search.domain.validator;

import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Comparator;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightShortestDurationComparator implements Comparator<FlightSearchViewModel> {
    @Override
    public int compare(FlightSearchViewModel first, FlightSearchViewModel second) {
        return first.getDurationMinute() - second.getDurationMinute();
    }
}
