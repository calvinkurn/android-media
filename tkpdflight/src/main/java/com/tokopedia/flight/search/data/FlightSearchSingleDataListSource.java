package com.tokopedia.flight.search.data;

import com.tokopedia.flight.search.data.cache.FlightSearchSingleDataCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource;
import com.tokopedia.flight.search.data.db.FlightSearchSingleDataDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchSingleDataListSource(FlightSearchSingleDataCacheSource dataListCacheManager,
                                            FlightSearchSingleDataDBSource flightSearchSingleDataListDBSource,
                                            FlightSearchDataCloudSource dataListCloudManager) {
        super(dataListCacheManager, flightSearchSingleDataListDBSource, dataListCloudManager);
    }
}
