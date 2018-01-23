package com.tokopedia.flight.search.data.db;

import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataDBSource extends AbsFlightSearchDataDBSource {

    @Inject
    FlightSearchSingleDataDBSource(){

    }

    @Override
    protected Class<? extends FlightSearchSingleRouteDB> getDBClass() {
        return FlightSearchSingleRouteDB.class;
    }

    @Override
    protected void insertSingleFlightData(FlightSearchData flightSearchData) {
        FlightSearchSingleRouteDB flightSearchSingleRouteDB = new FlightSearchSingleRouteDB(flightSearchData);
        flightSearchSingleRouteDB.insert();
    }

}
