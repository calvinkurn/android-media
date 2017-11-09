package com.tokopedia.flight.search.domain.comparator;

import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Comparator;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightEarliestArrivalComparator implements Comparator<FlightSearchViewModel> {
    @Override
    public int compare(FlightSearchViewModel o1, FlightSearchViewModel o2) {
        return (int) (o1.getArrivalTimeInt() - o2.getArrivalTimeInt());
    }
}
