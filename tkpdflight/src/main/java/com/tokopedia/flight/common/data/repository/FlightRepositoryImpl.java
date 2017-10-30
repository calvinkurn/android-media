package com.tokopedia.flight.common.data.repository;

import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSearchParamUtil;
import com.tokopedia.flight.search.data.FlightSearchReturnDataListSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataListSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl implements FlightRepository {

    private FlightAirportDataListSource flightAirportDataListSource;

    private FlightSearchSingleDataListSource flightSearchSingleDataListSource;
    private FlightSearchReturnDataListSource flightSearchReturnDataListSource;

    public FlightRepositoryImpl(FlightAirportDataListSource flightAirportDataListSource,
                                FlightSearchSingleDataListSource flightSearchSingleDataListSource,
                                FlightSearchReturnDataListSource flightSearchReturnDataListSource) {
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }

    //TODO define query
    @Override
    public Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(RequestParams requestParams) {
        if (FlightSearchParamUtil.isReturning(requestParams)) {
            return flightSearchReturnDataListSource.getDataList();
        } else {
            return flightSearchSingleDataListSource.getDataList();
        }
    }
}
