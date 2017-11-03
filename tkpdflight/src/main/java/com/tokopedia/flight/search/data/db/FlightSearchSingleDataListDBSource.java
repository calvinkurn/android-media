package com.tokopedia.flight.search.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB_Table;
import com.tokopedia.flight.search.view.model.FlightFilterModel;

import javax.inject.Inject;

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

    @Override
    protected void insertSingleFlightData(FlightSearchData flightSearchData) {
        FlightSearchSingleRouteDB flightSearchSingleRouteDB = new FlightSearchSingleRouteDB(flightSearchData);
        flightSearchSingleRouteDB.insert();
    }

}
