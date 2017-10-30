package com.tokopedia.flight.common.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public interface FlightRepository {
    Observable<List<FlightAirportDB>> getAirportList(String query);
    Observable<List<FlightSearchSingleRouteDB>> getFlightSearch(boolean isReturningFlight);
}
