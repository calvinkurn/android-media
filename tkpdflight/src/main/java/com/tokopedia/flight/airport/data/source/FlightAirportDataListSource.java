package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.List;

import rx.Observable;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry> {

    private FlightAirportDataListDBSource dataListDBSource;

    public FlightAirportDataListSource(DataListCacheSource dataListCacheManager, FlightAirportDataListDBSource dataListDBManager, DataListCloudSource<FlightAirportCountry> dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
        this.dataListDBSource = dataListDBManager;
    }

    public Observable<List<FlightAirportDB>> getAirportList(String queryText) {
        return dataListDBSource.getAirportList(queryText);
    }
}
