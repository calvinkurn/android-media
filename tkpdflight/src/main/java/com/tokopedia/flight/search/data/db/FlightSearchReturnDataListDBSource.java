package com.tokopedia.flight.search.data.db;

import com.tokopedia.flight.search.data.db.model.FlightSearchReturnRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightSearchReturnDataListDBSource extends AbsFlightSearchDataListDBSource{

    @Inject
    public FlightSearchReturnDataListDBSource() {
    }

    @Override
    protected Class<? extends FlightSearchSingleRouteDB> getDBClass() {
        return FlightSearchReturnRouteDB.class;
    }
}
