package com.tokopedia.flight.search.data;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;
import com.tokopedia.flight.search.data.db.FlightSearchReturnDataListDBSource;

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
