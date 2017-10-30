package com.tokopedia.flight.airline.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airline.data.cache.FlightAirlineDataListCacheSource;
import com.tokopedia.flight.airline.data.cloud.FlightAirlineDataListCloudSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataListCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListSource extends DataListSource<AirlineData, FlightAirlineDB> {

    @Inject
    public FlightAirlineDataListSource(FlightAirlineDataListCacheSource flightAirlineDataListCacheSource,
                                       FlightAirlineDataListDBSource flightAirlineDataListDBSource,
                                       FlightAirlineDataListCloudSource flightAirlineDataListCloudSource) {
        super(flightAirlineDataListCacheSource, flightAirlineDataListDBSource, flightAirlineDataListCloudSource);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList(final String idToSearch) {
        final HashMap<String, Object> map = FlightAirlineParamUtil.generateMap(idToSearch);
        return getDataList(map);
    }

    public static HashMap<String, Object> generateGetParam(String idToSearch){
        return FlightAirlineParamUtil.generateMap(idToSearch);
    }
}
