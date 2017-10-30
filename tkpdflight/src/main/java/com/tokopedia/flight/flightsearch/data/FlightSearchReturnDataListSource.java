package com.tokopedia.flight.flightsearch.data;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.flightsearch.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.flightsearch.data.db.FlightSearchReturnDataListDBSource;
import com.tokopedia.flight.flightsearch.data.db.FlightSearchSingleDataListDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchReturnDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchReturnDataListSource(DataListCacheSource dataListCacheManager,
                                            FlightSearchReturnDataListDBSource flightSearchReturnDataListDBSource,
                                            DataListCloudSource<FlightSearchData> dataListCloudManager) {
        super(dataListCacheManager, flightSearchReturnDataListDBSource, dataListCloudManager);
    }
}
