package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry> {

    private FlightAirportDataListDBSource dataListDBSource;

    @Inject
    public FlightAirportDataListSource(DataListCacheSource dataListCacheManager, FlightAirportDataListDBSource dataListDBManager, DataListCloudSource<FlightAirportCountry> dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
        this.dataListDBSource = dataListDBManager;
    }

    public Observable<List<FlightAirportDB>> getAirportList(final String queryText) {
        HashMap<String, Object> params = new HashMap();
        params.put(FlightAirportDataListCloudSource.KEYWORD, queryText);
        return updateLatestData(params).flatMap(new Func1<Boolean, Observable<List<FlightAirportDB>>>() {
            @Override
            public Observable<List<FlightAirportDB>> call(Boolean isDataAvailable) {
                return dataListDBSource.getAirportList(queryText);
            }
        });
    }
}
