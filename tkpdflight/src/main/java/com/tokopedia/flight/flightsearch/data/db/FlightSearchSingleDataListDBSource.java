package com.tokopedia.flight.flightsearch.data.db;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.flightsearch.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.flightsearch.data.db.model.FlightSearchSingleRouteDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataListDBSource extends AbsFlightSearchDataListDBSource{

    @Inject
    FlightSearchSingleDataListDBSource(){

    }

    @Override
    protected Class<? extends FlightSearchSingleRouteDB> getDBClass() {
        return FlightSearchSingleRouteDB.class;
    }
}
