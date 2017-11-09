package com.tokopedia.flight.search.data;

import com.tokopedia.flight.search.data.cache.FlightSearchSingleDataListCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataListCloudSource;
import com.tokopedia.flight.search.data.db.FlightSearchSingleDataListDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchSingleDataListSource(FlightSearchSingleDataListCacheSource dataListCacheManager,
                                            FlightSearchSingleDataListDBSource flightSearchSingleDataListDBSource,
                                            FlightSearchDataListCloudSource dataListCloudManager) {
        super(dataListCacheManager, flightSearchSingleDataListDBSource, dataListCloudManager);
    }
}
