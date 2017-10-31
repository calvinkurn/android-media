package com.tokopedia.flight.common.data.repository;

import com.tokopedia.flight.airline.data.FlightAirlineDataListSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.data.cloud.FlightClassesDataSource;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.search.data.FlightSearchReturnDataListSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataListSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightRepositoryImpl implements FlightRepository {

    private FlightAirportDataListSource flightAirportDataListSource;
    private FlightAirlineDataListSource flightAirlineDataListSource;
    private FlightClassesDataSource flightClassesDataSource;

    private FlightSearchSingleDataListSource flightSearchSingleDataListSource;
    private FlightSearchReturnDataListSource flightSearchReturnDataListSource;

    public FlightRepositoryImpl(FlightAirportDataListSource flightAirportDataListSource,
                                FlightAirlineDataListSource flightAirlineDataListSource,
                                FlightSearchSingleDataListSource flightSearchSingleDataListSource,
                                FlightSearchReturnDataListSource flightSearchReturnDataListSource,
                                FlightClassesDataSource flightClassesDataSource) {
        this.flightAirportDataListSource = flightAirportDataListSource;
        this.flightAirlineDataListSource = flightAirlineDataListSource;
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
        this.flightClassesDataSource = flightClassesDataSource;
    }

    @Override
    public Observable<List<FlightAirportDB>> getAirportList(String query) {
        return flightAirportDataListSource.getAirportList(query);
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return getAirlineList(null);
    }

    @Override
    public Observable<List<FlightAirlineDB>> getAirlineList(String airlineId) {
        return flightAirlineDataListSource.getAirlineList(airlineId);
    }

    @Override
    public Observable<List<FlightClassEntity>> getFlightClasses() {
        return flightClassesDataSource.getClasses();
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
