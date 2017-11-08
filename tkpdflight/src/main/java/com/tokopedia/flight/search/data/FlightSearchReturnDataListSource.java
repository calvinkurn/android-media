package com.tokopedia.flight.search.data;

import com.tokopedia.flight.search.data.cache.FlightSearchReturnDataListCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataListCloudSource;
import com.tokopedia.flight.search.data.db.FlightSearchReturnDataListDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchReturnDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchReturnDataListSource(FlightSearchReturnDataListCacheSource dataListCacheManager,
                                            FlightSearchReturnDataListDBSource flightSearchReturnDataListDBSource,
                                            FlightSearchDataListCloudSource dataListCloudManager) {
        super(dataListCacheManager, flightSearchReturnDataListDBSource, dataListCloudManager);
    }
}
