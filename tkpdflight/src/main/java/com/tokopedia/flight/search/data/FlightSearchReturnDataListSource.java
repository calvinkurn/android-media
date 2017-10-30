package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.search.data.cache.FlightSearchReturnDataListCacheSource;
import com.tokopedia.flight.search.data.cache.FlightSearchSingleDataListCacheSource;
import com.tokopedia.flight.search.data.cloud.FlightSearchDataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
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
