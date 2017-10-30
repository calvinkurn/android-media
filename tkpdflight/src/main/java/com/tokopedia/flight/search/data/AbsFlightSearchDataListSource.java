package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.AbsFlightSearchDataListDBSource;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;

import java.util.List;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class AbsFlightSearchDataListSource extends DataListSource<FlightSearchData, FlightSearchSingleRouteDB> {

    public AbsFlightSearchDataListSource(DataListCacheSource dataListCacheManager,
                                         AbsFlightSearchDataListDBSource absFlightSearchDataListDBSource,
                                         DataListCloudSource<FlightSearchData> dataListCloudManager) {
        super(dataListCacheManager, absFlightSearchDataListDBSource, dataListCloudManager);
    }

    public Observable<List<FlightSearchSingleRouteDB>> getDataList() {
//        final HashMap<String, Object> map = generateGetParam(queryText);
//        return getDataList(map);
        //TODO set query
        return getDataList(null);
    }

    //TODO set query
//    public static HashMap<String, Object> generateGetParam(String query){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put(FlightAirportDataListCloudSource.KEYWORD, query);
//        return map;
//    }
//
//    public static String getQueryFromMap(HashMap<String, Object> params){
//        return (String) params.get(FlightAirportDataListCloudSource.KEYWORD);
//    }
}
