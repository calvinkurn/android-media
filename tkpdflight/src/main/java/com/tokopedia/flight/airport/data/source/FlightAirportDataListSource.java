package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListFileSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry, FlightAirportDB> {

    public static final String ID_COUNTRY = "ID_COUNTRY";
    public static final String CITY_CODE = "CITY_CODE";
    public static final String AIRPORT_ID = "AIRPORT_ID";
    private FlightAirportDataListDBSource flightAirportDataListDBSource;
    private FlightAirportDataListFileSource flightAirportDataListFileSource;

    @Inject
    public FlightAirportDataListSource(FlightAirportDataCacheSource dataListCacheManager,
                                       FlightAirportDataListDBSource dataListDBManager,
                                       FlightAirportDataListFileSource dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
        flightAirportDataListDBSource = dataListDBManager;
        flightAirportDataListFileSource = dataListCloudManager;
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

    public static String getIdCountryFromMap(HashMap<String, Object> params) {
        if (params == null) {
            return "";
        }
        return (String) params.get(ID_COUNTRY);
    }

    public Observable<List<FlightAirportDB>> getAirportList(final String queryText) {
        final HashMap<String, Object> map = generateGetParam(queryText);
        return getDataList(map);
    }

    public Observable<List<FlightAirportDB>> getAirportList(String query, String idCountry) {
        HashMap<String, Object> map = generateGetParam(query);
        map.put(ID_COUNTRY, idCountry);
        return getDataList(map);
    }

    public Observable<List<FlightAirportDB>> getPhoneCodeList(String query) {
        return flightAirportDataListDBSource.getPhoneCodeList(query);
    }

    public Observable<FlightAirportDB> getAirport(final String airportCode) {

        return flightAirportDataListDBSource.isDataAvailable().flatMap(new Func1<Boolean, Observable<FlightAirportDB>>() {
            @Override
            public Observable<FlightAirportDB> call(Boolean aBoolean) {
                if (aBoolean) {
                    return flightAirportDataListDBSource.getAirport(airportCode);
                } else {
                    return getCloudData(new HashMap<String, Object>())
                            .flatMap(new Func1<List<FlightAirportDB>, Observable<FlightAirportDB>>() {
                                @Override
                                public Observable<FlightAirportDB> call(List<FlightAirportDB> flightAirportDBS) {
                                    return Observable.from(flightAirportDBS)
                                            .filter(new Func1<FlightAirportDB, Boolean>() {
                                                @Override
                                                public Boolean call(FlightAirportDB airportDB) {
                                                    return airportDB.getAirportId().equalsIgnoreCase(airportCode);
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }

    public Observable<FlightAirportDB> getAirport(final Map<String, String> params){
        return flightAirportDataListDBSource.isDataAvailable().flatMap(new Func1<Boolean, Observable<FlightAirportDB>>() {
            @Override
            public Observable<FlightAirportDB> call(Boolean aBoolean) {
                if (aBoolean) {
                    return flightAirportDataListDBSource.getAirport(params);
                } else {
                    return getCloudData(new HashMap<String, Object>())
                            .flatMap(new Func1<List<FlightAirportDB>, Observable<FlightAirportDB>>() {
                                @Override
                                public Observable<FlightAirportDB> call(List<FlightAirportDB> flightAirportDBS) {
                                    return Observable.from(flightAirportDBS)
                                            .filter(new Func1<FlightAirportDB, Boolean>() {
                                                @Override
                                                public Boolean call(FlightAirportDB flightAirportDB) {
                                                    for (Map.Entry<String, String> entry : params.entrySet()) {
                                                        if (entry.getKey().equals(FlightAirportDataListSource.CITY_CODE)) {
                                                            return flightAirportDB.getCityCode().equalsIgnoreCase(entry.getValue());
                                                        } else if (entry.getKey().equals(FlightAirportDataListSource.AIRPORT_ID)) {
                                                            return flightAirportDB.getAirportId().equalsIgnoreCase(entry.getValue());
                                                        }
                                                    }

                                                    return false;
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }
}
