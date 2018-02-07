package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.comparator.FlightCheapestComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestArrivalComparator;
import com.tokopedia.flight.search.domain.comparator.FlightEarliestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLatestArrivalComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLatestDepartureComparator;
import com.tokopedia.flight.search.domain.comparator.FlightLongestDurationComparator;
import com.tokopedia.flight.search.domain.comparator.FlightMostExpensiveComparator;
import com.tokopedia.flight.search.domain.comparator.FlightShortestDurationComparator;
import com.tokopedia.flight.search.domain.mapper.FlightSortMapper;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
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
    private FlightSearchUseCase flightSearchUseCase;
    private FlightSortMapper flightSortMapper;

    @Inject
    public FlightSearchWithSortUseCase(FlightSearchUseCase flightSearchUseCase, FlightSortMapper flightSortMapper) {
        this.flightSearchUseCase = flightSearchUseCase;
        this.flightSortMapper = flightSortMapper;
    }

    @Override
    public Observable<List<FlightSearchViewModel>> createObservable(final RequestParams requestParams) {
        return flightSearchUseCase.createObservable(requestParams)
                .map(flightSortMapper.withSortOptionId(FlightSearchParamUtil.getSortOptionId(requestParams)));
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        flightSearchUseCase.unsubscribe();
    }
}
