package com.tokopedia.flight.airline.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airline.data.cache.FlightAirlineDataCacheSource;
import com.tokopedia.flight.airline.data.cloud.FlightAirlineDataListCloudSource;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.util.FlightAirlineParamUtil;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListSource extends DataListSource<AirlineData, FlightAirlineDB> {

    @Inject
    public FlightAirlineDataListSource(FlightAirlineDataCacheSource flightAirlineDataListCacheSource,
                                       FlightAirlineDataListDBSource flightAirlineDataListDBSource,
                                       FlightAirlineDataListCloudSource flightAirlineDataListCloudSource) {
        super(flightAirlineDataListCacheSource, flightAirlineDataListDBSource, flightAirlineDataListCloudSource);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList(final String idToSearch) {
        final HashMap<String, Object> map = generateGetParam(idToSearch);
        return getDataList(map);
    }

    public Observable<List<FlightAirlineDB>> getAirlineList() {
        return getDataList(null);
    }

    public Observable<List<FlightAirlineDB>> getAirlineCacheList() {
        return getCacheDataList(null);
    }

    public Observable<List<FlightAirlineDB>> getAirlineCacheList(String airlineID) {
        final HashMap<String, Object> map = generateGetParam(airlineID);
        return getCacheDataList(map);
    }

    public static HashMap<String, Object> generateGetParam(String idToSearch) {
        return FlightAirlineParamUtil.generateMap(idToSearch);
    }
}
