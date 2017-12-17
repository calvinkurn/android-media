package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListFileSource;
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

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry, FlightAirportDB> {

    public static final String ID_COUNTRY = "ID_COUNTRY";

    @Inject
    public FlightAirportDataListSource(FlightAirportDataCacheSource dataListCacheManager,
                                       FlightAirportDataListDBSource dataListDBManager,
                                       FlightAirportDataListFileSource dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
    }

    public Observable<List<FlightAirportDB>> getAirportList(final String queryText) {
        final HashMap<String, Object> map = generateGetParam(queryText);
        return getDataList(map);
    }

    public static HashMap<String, Object> generateGetParam(String query) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(FlightAirportDataListFileSource.KEYWORD, query);
        return map;
    }

    public static HashMap<String, Object> generateGetCacheParam(String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(FlightAirportDataListDBSource.ID, id);
        return map;
    }

    public static String getQueryFromMap(HashMap<String, Object> params) {
        if (params == null) {
            return "";
        }
        return (String) params.get(FlightAirportDataListFileSource.KEYWORD);
    }

    public static String getIDFromMap(HashMap<String, Object> params) {
        if (params == null) {
            return "";
        }
        return (String) params.get(FlightAirportDataListDBSource.ID);
    }

    public static String getIdCountryFromMap(HashMap<String, Object> params){
        if (params == null) {
            return "";
        }
        return (String) params.get(ID_COUNTRY);
    }

    public Observable<List<FlightAirportDB>> getAirportList(String query, String idCountry) {
        HashMap<String, Object> map = generateGetParam(query);
        map.put(ID_COUNTRY, idCountry);
        return getDataList(map);
    }
}
