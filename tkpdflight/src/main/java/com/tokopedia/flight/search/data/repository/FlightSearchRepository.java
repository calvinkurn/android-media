package com.tokopedia.flight.search.data.repository;

import com.tokopedia.flight.search.data.FlightSearchReturnDataListSource;
import com.tokopedia.flight.search.data.FlightSearchSingleDataListSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightSearchRepository {

    private FlightSearchSingleDataListSource flightSearchSingleDataListSource;
    private FlightSearchReturnDataListSource flightSearchReturnDataListSource;

    @Inject
    public FlightSearchRepository(FlightSearchSingleDataListSource flightSearchSingleDataListSource,
                                  FlightSearchReturnDataListSource flightSearchReturnDataListSource) {
        this.flightSearchSingleDataListSource = flightSearchSingleDataListSource;
        this.flightSearchReturnDataListSource = flightSearchReturnDataListSource;
    }

    //TODO define query
    public Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(boolean isReturningFlight) {
        if (isReturningFlight) {
            return flightSearchReturnDataListSource.getDataList();
        } else {
            return flightSearchSingleDataListSource.getDataList();
        }
    }
}
