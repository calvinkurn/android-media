package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataListCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry, FlightAirportDB> {

    @Inject
    public FlightAirportDataListSource(FlightAirportDataListCacheSource dataListCacheManager,
                                       FlightAirportDataListDBSource dataListDBManager,
                                       FlightAirportDataListCloudSource dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
    }

    public Observable<List<FlightAirportDB>> getAirportList(final String queryText) {
        final HashMap<String, Object> map = generateGetParam(queryText);
        return getDataList(map);
    }

    public static HashMap<String, Object> generateGetParam(String query){
        HashMap<String, Object> map = new HashMap<>();
        map.put(FlightAirportDataListCloudSource.KEYWORD, query);
        return map;
    }

    public static String getQueryFromMap(HashMap<String, Object> params){
        return (String) params.get(FlightAirportDataListCloudSource.KEYWORD);
    }
}
