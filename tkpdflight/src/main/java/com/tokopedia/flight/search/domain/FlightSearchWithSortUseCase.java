package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.comparator.FlightCheapestComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestArrivalComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLatestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLongestDurationComparator;
import com.tokopedia.flight.search.domain.comparator.FlightMostExpensiveComparator;
import com.tokopedia.flight.search.domain.comparator.FlightShortestDurationComparator;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightSearchWithSortUseCase extends UseCase<List<FlightSearchViewModel>> {
    public static final String PARAM_SORT = "PARAM_SORT";
    private FlightSearchUseCase flightSearchUseCase;

    @Inject
    public FlightSearchWithSortUseCase(FlightSearchUseCase flightSearchUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
    }

    @Override
    public Observable<List<FlightSearchViewModel>> createObservable(final RequestParams requestParams) {
        return flightSearchUseCase.createObservable(requestParams)
                .map(new Func1<List<FlightSearchViewModel>, List<FlightSearchViewModel>>() {
                    @Override
                    public List<FlightSearchViewModel> call(List<FlightSearchViewModel> flightSearchViewModels) {
                        switch (requestParams.getInt(PARAM_SORT, 0)) {
                            case FlightSortOption.EARLIEST_DEPARTURE:
                                Collections.sort(flightSearchViewModels, new FlightEarliestDepartureComparator());
                                break;
                            case FlightSortOption.LATEST_DEPARTURE:
                                Collections.sort(flightSearchViewModels, new FlightLatestDepartureComparator());
                                break;
                            case FlightSortOption.SHORTEST_DURATION:
                                Collections.sort(flightSearchViewModels, new FlightShortestDurationComparator());
                                break;
                            case FlightSortOption.LONGEST_DURATION:
                                Collections.sort(flightSearchViewModels, new FlightLongestDurationComparator());
                                break;
                            case FlightSortOption.EARLIEST_ARRIVAL:
                                Collections.sort(flightSearchViewModels, new FlightEarliestArrivalComparator());
                                break;
                            case FlightSortOption.LATEST_ARRIVAL:
                                Collections.sort(flightSearchViewModels, new FlightLongestDurationComparator());
                                break;
                            case FlightSortOption.CHEAPEST:
                                Collections.sort(flightSearchViewModels, new FlightCheapestComparator());
                                break;
                            case FlightSortOption.MOST_EXPENSIVE:
                                Collections.sort(flightSearchViewModels, new FlightMostExpensiveComparator());
                                break;

                        }
                        return flightSearchViewModels;
                    }
                });
    }

    public RequestParams createRequestParam(int sortId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(PARAM_SORT, sortId);
        return requestParams;
    }
}
