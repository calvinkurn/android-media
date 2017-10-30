package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSearchParamUtil;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightSearchUseCase extends UseCase<List<FlightSearchSingleRouteDB>> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightSearchUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightSearchSingleRouteDB>> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightSearch(requestParams);
    }

    public static RequestParams generateRequestParams(boolean isReturning){
        return FlightSearchParamUtil.generateRequestParams(isReturning);
    }

}
