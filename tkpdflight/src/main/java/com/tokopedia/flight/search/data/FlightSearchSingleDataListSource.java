package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.FlightSearchSingleDataListDBSource;

import javax.inject.Inject;

/**
 * @author normansyahputa on 5/18/17.
 */

public class FlightSearchSingleDataListSource extends AbsFlightSearchDataListSource {

    @Inject
    public FlightSearchSingleDataListSource(DataListCacheSource dataListCacheManager,
                                            FlightSearchSingleDataListDBSource flightSearchSingleDataListDBSource,
                                            DataListCloudSource<FlightSearchData> dataListCloudManager) {
        super(dataListCacheManager, flightSearchSingleDataListDBSource, dataListCloudManager);
    }
}
