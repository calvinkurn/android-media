package com.tokopedia.flight.airport.data.source;

import com.tokopedia.abstraction.base.data.source.DataListSource;
import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.base.data.source.database.DataListDBSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightAirportDataListSource extends DataListSource<FlightAirportCountry> {

    public FlightAirportDataListSource(DataListCacheSource dataListCacheManager, FlightAirportDataListDBSource dataListDBManager, DataListCloudSource<FlightAirportCountry> dataListCloudManager) {
        super(dataListCacheManager, dataListDBManager, dataListCloudManager);
    }
}
