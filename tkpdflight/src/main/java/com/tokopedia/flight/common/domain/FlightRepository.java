package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository {
    Observable<List<FlightAirportDB>> getAirportList(String query);

    Observable<List<FlightAirlineDB>> getAirlineList();
    Observable<List<FlightAirlineDB>> getAirlineList(String airlineId);

    Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(RequestParams requestParams);
}
