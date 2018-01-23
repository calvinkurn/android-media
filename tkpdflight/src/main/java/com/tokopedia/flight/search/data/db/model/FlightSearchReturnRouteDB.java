package com.tokopedia.flight.search.data.db.model;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Table;
import com.tokopedia.flight.common.database.TkpdFlightDatabase;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;

/**
 * @author sebastianuskh on 4/13/17.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchReturnRouteDB extends FlightSearchSingleRouteDB {

    public FlightSearchReturnRouteDB(){

    }
    public FlightSearchReturnRouteDB(FlightSearchData flightSearchData) {
        super(flightSearchData);
    }
}
