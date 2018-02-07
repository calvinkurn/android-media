package com.tokopedia.flight.search.domain.mapper;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.comparator.FlightCheapestComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestArrivalComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLatestArrivalComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLatestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLongestDurationComparator;
import com.tokopedia.flight.search.domain.comparator.FlightMostExpensiveComparator;
import com.tokopedia.flight.search.domain.comparator.FlightShortestDurationComparator;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by User on 11/9/2017.
 */

public class FlightSortMapper implements Func1<List<FlightSearchViewModel>, List<FlightSearchViewModel>> {

    private @FlightSortOption int sortOptionId;

    @Inject
    public FlightSortMapper(){

    }

    public FlightSortMapper withSortOptionId(int sortOptionId) {
        this.sortOptionId = sortOptionId;
        return this;
    }

    @Override
    public List<FlightSearchViewModel> call(List<FlightSearchViewModel> flightSearchViewModelList) {
        switch (sortOptionId) {
            case FlightSortOption.NO_PREFERENCE:
                return flightSearchViewModelList;
            case FlightSortOption.EARLIEST_DEPARTURE:
                Collections.sort(flightSearchViewModelList, new FlightEarliestDepartureComparator());
                break;
            case FlightSortOption.LATEST_DEPARTURE:
                Collections.sort(flightSearchViewModelList, new FlightLatestDepartureComparator());
                break;
            case FlightSortOption.SHORTEST_DURATION:
                Collections.sort(flightSearchViewModelList, new FlightShortestDurationComparator());
                break;
            case FlightSortOption.LONGEST_DURATION:
                Collections.sort(flightSearchViewModelList, new FlightLongestDurationComparator());
                break;
            case FlightSortOption.EARLIEST_ARRIVAL:
                Collections.sort(flightSearchViewModelList, new FlightEarliestArrivalComparator());
                break;
            case FlightSortOption.LATEST_ARRIVAL:
                Collections.sort(flightSearchViewModelList, new FlightLatestArrivalComparator());
                break;
            case FlightSortOption.CHEAPEST:
                Collections.sort(flightSearchViewModelList, new FlightCheapestComparator());
                break;
            case FlightSortOption.MOST_EXPENSIVE:
                Collections.sort(flightSearchViewModelList, new FlightMostExpensiveComparator());
                break;
            default:
                return flightSearchViewModelList;
        }
        return flightSearchViewModelList;
    }
}
